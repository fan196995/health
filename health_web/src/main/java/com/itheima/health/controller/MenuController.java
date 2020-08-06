package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/6 11:41
 */
@RestController
@RequestMapping(value = "/menu")
public class MenuController {

    @Reference
    private MenuService menuService;

    @GetMapping(value = "/findAll")
    public Result findAll(){
       List<Menu> menuList =  menuService.findAll();
       return new Result(true, MessageConstant.QUERY_MENU_SUCCESS,menuList);
    }
}
