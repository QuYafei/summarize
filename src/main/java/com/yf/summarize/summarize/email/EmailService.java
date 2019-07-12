package com.yf.summarize.summarize.email;

import com.alibaba.fastjson.JSONObject;
import com.yf.summarize.summarize.message.MessageAdapter;
import com.yf.summarize.summarize.redis.RedisService;
import com.yf.summarize.summarize.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.subject}")
    private String subject;

    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisService redisService;

    @Override
    public void senMsg(JSONObject body) throws MessagingException {
        //处理发送邮件
        String email = body.getString("email");
        if (StringUtils.isEmpty(email)) {
            return;
        }
        log.info("消息服务平台发送邮件：{},开始", email);

//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        // 来自账号 自己发自己
//        simpleMailMessage.setFrom("13271531318@163.com");
//        // 发送账号
//        simpleMailMessage.setTo(email);
//        // 标题
//        simpleMailMessage.setSubject(subject);
//        //内容
//        simpleMailMessage.setText(text.replace("{}",email));
//
//        javaMailSender.send(simpleMailMessage);
        // 创建多用途邮件消息对象
        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        // 创建邮件消息助手
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);

        messageHelper.setTo(email);
        messageHelper.setFrom("13271531318@163.com");
        messageHelper.setSubject(subject);

        //参数2:以html格式发送
//        String test = "发送成功";
//        String text = "注册成功2，<a href=\"http://127.0.0.1:8080/userActivate" + "?code=" + mailCode
//                + "&email=" + email + "\">点击激活</a>";
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        redisService.setString(email, code + "", Constants.CODE_TIME);
        log.info("生成邮箱验证码存入Redis中：{}", code);

        mailMessage.setContent(text.replace("{}", code), "text/html;charset=UTF-8");
        javaMailSender.send(mailMessage);

        log.info("消息服务平台发送邮件：{},完成", email);
    }
}
