package com.yf.summarize.summarize.message;

import com.aliyuncs.exceptions.ClientException;

public interface PhoneAdapter {

    int sendPhone(String phone) throws ClientException;

}
