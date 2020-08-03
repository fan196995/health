package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;

import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanbo
 * @date 2020/8/2 18:42
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Reference
    private UserService userService;

    @GetMapping(value = "/getUsername")
    public Result getUsername(){
        //登录用户认证信息
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(loginUser.getUsername());
        //登录用户名
        if (loginUser!=null){
            String username = loginUser.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }
        return new Result(false, MessageConstant.GET_USERNAME_FAIL);
    }
}
