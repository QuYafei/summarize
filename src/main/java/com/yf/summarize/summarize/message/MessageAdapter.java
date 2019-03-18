package com.yf.summarize.summarize.message;

import com.alibaba.fastjson.JSONObject;

//同意发送短信接口
public interface MessageAdapter {

    void senMsg(JSONObject body);
}
