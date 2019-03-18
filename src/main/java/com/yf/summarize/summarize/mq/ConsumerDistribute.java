package com.yf.summarize.summarize.mq;

import com.alibaba.fastjson.JSONObject;
import com.yf.summarize.summarize.email.EmailService;
import com.yf.summarize.summarize.message.MessageAdapter;
import com.yf.summarize.summarize.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class ConsumerDistribute {

    @Autowired
    private EmailService emailService;
    private MessageAdapter messageAdapter;

    @JmsListener(destination = "messages_queue")
    public void distribute(String json){
        log.info("#####消息服务平台接受消息内容:{}#####",json);

        if (StringUtils.isEmpty(json)){
            return;
        }

        JSONObject rootJSON = new JSONObject().parseObject(json);
        JSONObject header = rootJSON.getJSONObject("header");
        String interfaceType = header.getString("interfaceType");

        if (StringUtils.isEmpty(interfaceType)){
            return;
        }

        //判断接口类型是否为发送邮件

        if (interfaceType.equals(Constants.MSG_EMAIL)){
            messageAdapter = emailService;
        }
        if (messageAdapter == null){
            return;
        }

        JSONObject contentJson = rootJSON.getJSONObject("content");
        messageAdapter.senMsg(contentJson);

    }

}
