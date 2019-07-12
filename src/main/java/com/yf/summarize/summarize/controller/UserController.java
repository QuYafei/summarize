package com.yf.summarize.summarize.controller;


import com.aliyuncs.exceptions.ClientException;
import com.yf.summarize.summarize.dto.BindingEmailDTO;
import com.yf.summarize.summarize.dto.RegUserDTO;
import com.yf.summarize.summarize.dto.UserLoginDTO;
import com.yf.summarize.summarize.service.UserService;
import com.yf.summarize.summarize.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "手机验证")
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultVO sendPhone(String phone) throws ClientException {

        ResultVO resultVO = userService.sendPhone(phone);

        return resultVO;
    }


    @ApiOperation(value = "注册用户")
    @RequestMapping(value = "/regUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultVO regUser(@RequestBody RegUserDTO regUserDTO) throws IOException {

        ResultVO resultVO = userService.regUser(regUserDTO);

        return resultVO;
    }

    @ApiOperation(value = "根据userId查询用户")
    @RequestMapping(value = "/selectById/{userId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultVO selectByUserId(@PathVariable("userId") int userId) {

        ResultVO selectById = userService.selectById(userId);

        return selectById;
    }


    @ApiOperation(value = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultVO login(@RequestBody UserLoginDTO loginDTO) {

        ResultVO login = userService.userLogin(loginDTO);

        return login;
    }

    @ApiOperation(value = "根据token查询信息")
    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultVO findByTokenUser(@RequestParam("token") String token) {

        ResultVO byTokenUser = userService.findByTokenUser(token);

        return byTokenUser;
    }

    @ApiOperation(value = "发送邮箱获取验证码")
    @RequestMapping(value = "/codeEmail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ResultVO getEmailCode(@RequestParam("email") String email) {

        ResultVO resultVO = userService.emailCode(email);

        return resultVO;
    }

    @ApiOperation(value = "绑定邮箱")
    @RequestMapping(value = "/bindEmail", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public ResultVO bindingEmail(@RequestBody BindingEmailDTO bindingEmailDTO) throws IOException {

        ResultVO resultVO = userService.bindingEmail(bindingEmailDTO);

        return resultVO;
    }

    @ApiOperation(value = "找到所有的用户")
    @RequestMapping(value = "/bindEmail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public ResultVO getAllUser(){

        ResultVO resultVO = userService.getAllUser();

        return resultVO;
    }



}
