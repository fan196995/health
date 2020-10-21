package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Tree;
import com.itheima.health.service.MenuService;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/getcode")
    public Result getcode () {
            //获取后端的全部数据
    List<Menu> list = menuService.getcode();
    //要返回给前端的数据
    List<Menu> resultList = new ArrayList<Menu>();
    Map<Object, Object> treeMap = new HashMap<>();
    Object itemTree;
    //循环后端传来的数据
            for (int i = 0; i < list.size() && !list.isEmpty(); i++) {
        //获取单条的数据
        itemTree = list.get(i);
        //用ID 来当键 把每一条数据当值保存到map集合里面
        treeMap.put(list.get(i).getId(), itemTree);
    }
            for (int i = 0; i < list.size()&&!list.isEmpty(); i++) {
        if (!treeMap.containsKey(list.get(i).getParentMenuId())) {
            //如果parentMenuId的值为空代表他是父节点
            //把父节点的数据给存在要返回给前端的集合里面
            resultList.add(list.get(i));
        }
    }
    //继续循环数据 将子节点的数据放到对应的父节点的children属性里面
            for (int i = 0; i < list.size() && !list.isEmpty(); i++) {
        //获取有父节点的那条数据
        Menu menu = (Menu) treeMap.get(list.get(i).getParentMenuId());
        //如果有
        if (menu != null) {
            // 有了父节点，要判断父节点下存贮字节点的集合是否存在，然后将子节点放入
            if (menu.getChildren() == null) {
                //如果没有就创建一个
                menu.setChildren(new ArrayList<Menu>());
            }
            menu.getChildren().add(list.get(i));
            treeMap.put(list.get(i).getParentMenuId(), menu);
        }
    }
            return new Result(true,MessageConstant.QUERY_MENU_SUCCESS,resultList);
}

    }

