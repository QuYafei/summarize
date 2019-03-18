package com.yf.summarize.summarize.email;

import com.alibaba.fastjson.JSONObject;
import com.yf.summarize.summarize.message.MessageAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class EmailService implements MessageAdapter {

    @Value("${msg.subject}")
    private String subject;

    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void senMsg(JSONObject body) {
        //处理发送邮件
        String email = body.getString("email");
        if (StringUtils.isEmpty(email)){
            return ;
        }
        log.info("消息服务平台发送邮件：{},开始",email);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        // 来自账号 自己发自己
        simpleMailMessage.setFrom("13271531318@163.com");
        // 发送账号
        simpleMailMessage.setTo(email);
        // 标题
        simpleMailMessage.setSubject(subject);


        //内容
        simpleMailMessage.setText(text.replace("{}",email));
        javaMailSender.send(simpleMailMessage);
        log.info("消息服务平台发送邮件：{},完成",email);
    }
}
