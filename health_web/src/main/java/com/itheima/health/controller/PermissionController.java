package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/6 11:48
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    @GetMapping(value = "/findAll")
    public Result findAll(){
     List<Permission> permissionList =  permissionService.findAll();
     return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS,permissionList);
    }
}
