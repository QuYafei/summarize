package com.yf.summarize.summarize.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yf.summarize.summarize.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegUserDTO {

    @ApiModelProperty(name = "userName", value = "用户名称", required = true)
    private String userName;
    @ApiModelProperty(name = "password", value = "用户密码", required = true)
    private String password;
    @ApiModelProperty(name = "phone", value = "手机号", required = true)
    private String phone;
    @ApiModelProperty(name = "code", value = "验证码", required = true)
    private String code;

    public User userRegister() {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone(phone);
        return user;
    }


}
