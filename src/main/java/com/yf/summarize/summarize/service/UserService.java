package com.yf.summarize.summarize.service;

import com.alibaba.fastjson.JSONObject;
import com.yf.summarize.summarize.entity.User;
import com.yf.summarize.summarize.mapper.UserMapper;
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

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;

    @Value("${messages.queue}")
    private String MESSAGESQUEUE;

    public ResultVO regUser(User user) throws IOException {
        log.info("----------添加用户:{}",JacksonUtil.bean2Json(user));
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return ResultVOUtil.error("密码不能为空.");
        }
        String newPassword = MD5Util.MD5(password);
        user.setPassword(newPassword);
        Integer result = userMapper.insert(user);
        if (result <= 0) {
            return ResultVOUtil.error("注册用户信息失败.");
        }

        String email = user.getEmail();
        String json = emailJson(email);
        log.info("####会员服务推送消息到消息服务平台####json:{}", json);
        sendMsg(json);
        return ResultVOUtil.success("注册成功");

    }

    private String emailJson(String email){
        JSONObject rootJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put("interfaceType",Constants.MSG_EMAIL);
        JSONObject content = new JSONObject();
        content.put("email",email);
        rootJson.put("header",header);
        rootJson.put("content",content);
        return rootJson.toJSONString();
    }

    private void sendMsg(String json){
        ActiveMQQueue activeMQQueue = new ActiveMQQueue(MESSAGESQUEUE);
        registerMailboxProducer.sendMsg(activeMQQueue,json);
    }


    public ResultVO selectById(int userId){
        log.info("根据userId查询用户:{}",userId);
        if (userId == 0){
            return ResultVOUtil.error("userId不能为空");
        }
        User user = userMapper.selectByPrimaryKey(userId);
        return ResultVOUtil.success(user);
    }


    public ResultVO userLogin(User user){
        //1.验证参数
        String userName = user.getUserName();
        if (StringUtils.isEmpty(userName)){
            return ResultVOUtil.error(001,"用户名不能为空");
        }
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)){
            return ResultVOUtil.error(002,"密码不能为空");
        }
        //2.数据库查找账号密码是否正确
        String newPassword = MD5Util.MD5(password);
        User user1 = userMapper.login(userName, newPassword);
        if (StringUtils.isEmpty(user1)){
            return ResultVOUtil.error(500,"账号密码不正确");
        }
        //3.如果账号密码正确，对应生成token
        String token = TokenUtils.getToken();
        //4.存放在redis中，key为token，value为userId
        Long user1Id = user1.getId();
        log.info("----------------------用户信息token放入redis中....key为：{},value为：{}",token,user1Id);
        redisService.setString(token,user1Id+"", Constants.TOKEN_TIME);
        //5.返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token",token);
        return ResultVOUtil.success(jsonObject);
    }

    public ResultVO findByTokenUser(String token){
        //1.验证Token
        if (StringUtils.isEmpty(token)){
            return ResultVOUtil.error("token不能为空");
        }
        //2.从redis中使用token查询查找相应的userId
        String strUserId = (String) redisService.getString(token);
        if (StringUtils.isEmpty(strUserId)){
            return ResultVOUtil.error("Token无效或已过期");
        }
        //3.使用userId查询数据库返回数据给客户端
        long userId = Long.parseLong(strUserId);
        User user = userMapper.selectByPrimaryKey(userId);
        if (StringUtils.isEmpty(user)){
            return ResultVOUtil.error("未查到该信息");
        }
        user.setPassword(null);
        return ResultVOUtil.success(user);
    }

}
