package com.yf.summarize.summarize.mapper;

import com.yf.summarize.summarize.entity.User;
import com.yf.summarize.summarize.util.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface UserMapper extends BaseMapper<User> {

    User login(@Param("userName") String userName,@Param("password") String password);

    User findByTokenUser(@Param("token") String token);

}