package com.yf.summarize.summarize.util;

import java.util.UUID;

public class TokenUtils {

    public static String getToken(){

        return "USER"+"-"+ UUID.randomUUID();
    }
}
