package com.yf.summarize.summarize.controller;


import com.yf.summarize.summarize.entity.User;
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

    @ApiOperation(value = "添加用户")
    @RequestMapping(value = "/regUser", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO regUser(@RequestBody User user) throws IOException {

        ResultVO resultVO = userService.regUser(user);

        return resultVO;
    }

    @ApiOperation(value = "根据userId查询用户")
    @RequestMapping(value = "/selectById/{userId}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO selectByUserId(@PathVariable("userId") int userId){

        ResultVO selectById = userService.selectById(userId);

        return selectById;
    }


    @ApiOperation(value = "登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO login(@RequestBody User user){

        ResultVO login = userService.userLogin(user);

        return login;
    }

    @ApiOperation(value = "根据token查询信息")
    @RequestMapping(value = "/token", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO findByTokenUser(@RequestParam("token") String token){

        ResultVO byTokenUser = userService.findByTokenUser(token);

        return byTokenUser;
    }

}
