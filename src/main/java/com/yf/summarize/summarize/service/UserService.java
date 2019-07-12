package com.yf.summarize.summarize.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.yf.summarize.summarize.dto.BindingEmailDTO;
import com.yf.summarize.summarize.dto.RegUserDTO;
import com.yf.summarize.summarize.dto.UserLoginDTO;
import com.yf.summarize.summarize.entity.User;
import com.yf.summarize.summarize.mapper.UserMapper;
import com.yf.summarize.summarize.message.PhoneAdapter;
import com.yf.summarize.summarize.mq.RegisterMailboxProducer;
import com.yf.summarize.summarize.redis.RedisService;
import com.yf.summarize.summarize.util.*;
import com.yf.summarize.summarize.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;

    @Autowired
    private PhoneAdapter phoneAdapter;

    @Value("${messages.queue}")
    private String MESSAGESQUEUE;

    /**
     * 注册用户
     *
     * @param regUserDTO
     * @return
     * @throws IOException
     */
    public ResultVO regUser(RegUserDTO regUserDTO) throws IOException {
        log.info("----------添加用户:{}", JacksonUtil.bean2Json(regUserDTO));
        String password = regUserDTO.getPassword();

        if (!RegexUtil.isMobileExact(regUserDTO.getPhone())) {
            return ResultVOUtil.error("手机号格式不正确.");
        }

        if (StringUtils.isEmpty(password)) {
            return ResultVOUtil.error("密码不能为空.");
        }
        //MD5加密入库
        String newPassword = MD5Util.MD5(password);
        regUserDTO.setPassword(newPassword);

        String getCode = regUserDTO.getCode();
        if (getCode == null) {
            return ResultVOUtil.error("验证码不能为空");
        }
        String phoneCode = (String) redisService.getString(regUserDTO.getPhone());
        log.info("取出Redis中的验证码：{}", phoneCode);
        if (!phoneCode.equals(getCode)) {
            return ResultVOUtil.error("验证码不正确或超时");
        }

        int result = userMapper.insertSelective(regUserDTO.userRegister());

        if (result <= 0) {
            return ResultVOUtil.error("注册用户信息失败.");
        }
        return ResultVOUtil.success("注册成功");
    }

    /**
     * 发送短信
     *
     * @param phone
     * @return
     * @throws ClientException
     */
    public ResultVO sendPhone(String phone) throws ClientException {
        if (!RegexUtil.isMobileExact(phone)) {
            return ResultVOUtil.error("手机号格式不正确.");
        }
        int i = phoneAdapter.sendPhone(phone);
        if (i <= 0) {
            return ResultVOUtil.error("手机短信发送失败");
        } else {
            return ResultVOUtil.success("注册成功");
        }
    }

    /**
     * 绑定邮箱
     *
     * @param
     * @param
     * @return
     */
    public ResultVO bindingEmail(BindingEmailDTO bindingEmailDTO) throws IOException {
        String email = bindingEmailDTO.getEmail();
        if (!RegexUtil.isEmail(email)) {
            return ResultVOUtil.error("邮箱格式不正确");
        }
        String code = bindingEmailDTO.getCode();
        if (org.apache.commons.lang3.StringUtils.isBlank(code)) {
            return ResultVOUtil.error("验证码不能为空");
        }
        String token = bindingEmailDTO.getToken();
        long userId = TokenUser(token);
        if (userId <= 0) {
            return ResultVOUtil.error("Token不能为空,无效或已过期");
        }
        bindingEmailDTO.setId(userId);
        String codeEmail = (String) redisService.getString(email);
        log.info("取出Redis中的验证码：{}", codeEmail);
        if (!code.equals(codeEmail)) {
            return ResultVOUtil.error("邮箱验证码不正确或超时");
        }
        log.info(String.format("绑定邮箱数据：{}",JacksonUtil.bean2Json(bindingEmailDTO)));
        int updateBindingEmail = userMapper.updateByPrimaryKeySelective(bindingEmailDTO.getUser());
        if (updateBindingEmail <= 0) {
            return ResultVOUtil.error("修改失败");
        }
        return ResultVOUtil.error("修改成功");
    }


    /**
     * 发送邮箱验证码
     *
     * @param email
     * @param
     * @return
     */
    public ResultVO emailCode(String email) {
        if (!RegexUtil.isEmail(email)) {
            return ResultVOUtil.error("邮箱格式不正确");
        }
        String json = emailJson(email);
        log.info("####会员服务推送消息到消息服务平台####json:{}", json);
        try {
            sendMsg(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVOUtil.success("发送邮箱成功！");
    }

    private String emailJson(String email) {
        JSONObject rootJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType", Constants.MSG_EMAIL);
        JSONObject content = new JSONObject();
        content.put("email", email);
        rootJson.put("header", header);
        rootJson.put("content", content);
        return rootJson.toJSONString();
    }

    private void sendMsg(String json) {
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue, json);
    }


    /**
     * 根据id查询用户
     *
     * @param userId
     * @return
     */
    public ResultVO selectById(int userId) {
        log.info("根据userId查询用户:{}", userId);
        if (userId == 0) {
            return ResultVOUtil.error("userId不能为空");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        return ResultVOUtil.success(user);
    }


    /**
     * 用户登陆
     *
     * @param
     * @return
     */
    public ResultVO userLogin(UserLoginDTO userLoginDTO) {
        String userName = userLoginDTO.getUserName();
        //1.验证参数
        if (StringUtils.isEmpty(userName)) {
            return ResultVOUtil.error(001, "用户名不能为空");
        }
        String password = userLoginDTO.getPassword();
        if (StringUtils.isEmpty(password)) {
            return ResultVOUtil.error(002, "密码不能为空");
        }
        //2.数据库查找账号密码是否正确
        String newPassword = MD5Util.MD5(password);
        User user1 = userMapper.login(userName, newPassword);
        if (StringUtils.isEmpty(user1)) {
            return ResultVOUtil.error(500, "账号密码不正确");
        }
        //3.如果账号密码正确，对应生成token
        String token = TokenUtils.getToken();
        //4.存放在redis中，key为token，value为userId
        Long user1Id = user1.getId();
        log.info("----------------------用户信息token放入redis中....key为：{},value为：{}", token, user1Id);
        redisService.setString(token, user1Id + "", Constants.TOKEN_TIME);
        //5.返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", token);
        return ResultVOUtil.success(jsonObject);
    }

    /**
     * 获取token的值
     *
     * @param token
     * @return
     */
    public long TokenUser(String token) {
        //1.验证Token
        if (StringUtils.isEmpty(token)) {
            return 0;
        }
        //2.从redis中使用token查询查找相应的userId
        String strUserId = (String) redisService.getString(token);
        log.info("取出redis中token对应的id：{}", strUserId);
        if (StringUtils.isEmpty(strUserId)) {
            return 0;
        }
        //3.使用userId查询数据库返回数据给客户端
        long userId = Long.parseLong(strUserId);

        return userId;
    }

    /**
     * 根据Token查询用户
     *
     * @param token
     * @return
     */
    public ResultVO findByTokenUser(String token) {
//        //1.验证Token
//        if (StringUtils.isEmpty(token)){
//            return ResultVOUtil.error("token不能为空");
//        }
//        //2.从redis中使用token查询查找相应的userId
//        String strUserId = (String) redisService.getString(token);
//        if (StringUtils.isEmpty(strUserId)){
//            return ResultVOUtil.error("Token无效或已过期");
//        }
//        //3.使用userId查询数据库返回数据给客户端
//        long userId = Long.parseLong(strUserId);

        long userId = TokenUser(token);
        if (userId <= 0) {
            return ResultVOUtil.error("Token不能为空,无效或已过期");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)) {
            return ResultVOUtil.error("未查到该信息");
        }
        user.setPassword(null);
        return ResultVOUtil.success(user);
    }

    /**
     * 找到所有的用户
     * @return
     */
    public ResultVO getAllUser(){

        List<User> users = userMapper.selectAll();
        for (User user:users){
            user.setPassword(null);
        }
        return ResultVOUtil.success(users);

    }

}
