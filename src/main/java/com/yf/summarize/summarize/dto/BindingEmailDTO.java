package com.yf.summarize.summarize.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yf.summarize.summarize.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BindingEmailDTO {

    @ApiModelProperty(name = "email", value = "邮箱", required = true)
    private String email;
    @ApiModelProperty(name = "code", value = "验证码", required = true)
    private String code;
    @ApiModelProperty(name = "token", value = "身份认证", required = true)
    private String token;
    @JsonIgnore
    @ApiModelProperty(name = "id", value = "Token对应的id", required = true)
    private long id;


    public User getUser() {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        return user;
    }
}
