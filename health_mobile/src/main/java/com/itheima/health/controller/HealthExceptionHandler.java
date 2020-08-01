package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
//封装了注解@ResponseBody
@RestControllerAdvice
public class HealthExceptionHandler {

    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return new Result(false, "操作失败，发生未知异常，请联系管理员");
    }
}
