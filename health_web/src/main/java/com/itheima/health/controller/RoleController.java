package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/5 19:24
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    @Reference
    private RoleService roleService;

    @PostMapping(value = "/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Role> pageResult = roleService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS,pageResult);
    }

    @PostMapping(value = "/add")
    public Result add(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        roleService.add(role,permissionIds,menuIds);
        return new Result(true,MessageConstant.ADD_ROLE_SUCCESS);
    }

    @GetMapping(value = "/findAll")
    public Result findAll(){
        List<Role> roleList =roleService.findAll();
        return new Result(true,MessageConstant.QUERY_ROLELIST_SUCCESS,roleList);
    }

    @GetMapping(value = "/findById")
    public Result findById(int id){
        Role role = roleService.findById(id);
        return new Result(true,MessageConstant.QUERY_ROLE_SUCCESS,role);
    }

    @GetMapping(value = "/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(int id){
        List<Integer> permissionIds = roleService.findPermissionIdsByRoleId(id);
        return new Result(true,MessageConstant.QUERY_PERMISSION_SUCCESS,permissionIds);
    }

    @GetMapping(value = "/findMenuIdsByRoleId")
    public Result findMenuIdsByRoleId(int id){
        List<Integer> menuIds = roleService.findMenuIdsByRoleId(id);
        return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,menuIds);
    }

    @PostMapping (value = "/update")
    public Result update(@RequestBody Role role,Integer[] permissionIds,Integer[] menuIds){
        roleService.update(role,permissionIds,menuIds);
        return new Result(true,MessageConstant.EDIT_ROLE_SUCCESS);
    }

    @PostMapping(value = "/deleteById")
    public Result deleteById(int id){
        roleService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_ROLE_SUCCESS);
    }

    @GetMapping(value = "/findRoleMenuIds")
    public Result findRoleMenuIds(int id){
        List<Integer> roleMenuIds = roleService.findRoleMenuIds(id);
        return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,roleMenuIds);
    }
}


