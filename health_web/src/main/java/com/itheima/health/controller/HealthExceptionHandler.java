package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
//封装了注解@ResponseBody
@RestControllerAdvice
public class HealthExceptionHandler {

//    自定义招出的异常处理
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        return new Result(false, e.getMessage());
    }

//    所有未知的异常
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return new Result(false, "操作失败，发生未知异常，请联系管理员");
    }


    //密码错误
    @ExceptionHandler(BadCredentialsException.class)
    public Result handBadCredentialsException(BadCredentialsException e){
        return handleUserPassword();
    }

    //用户名不存在
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handInternalAuthenticationServiceException(InternalAuthenticationServiceException e){
        return handleUserPassword();
    }

    private Result handleUserPassword(){
        return new Result(false, "用户名或密码错误");
    }


    //用户名不存在
    @ExceptionHandler(AccessDeniedException.class)
    public Result handAccessDeniedException(AccessDeniedException e){
        return new Result(false, "没有权限");
    }
}
