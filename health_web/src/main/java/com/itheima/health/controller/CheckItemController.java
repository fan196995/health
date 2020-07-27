package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@Controller+@Responsebody
@RestController
@RequestMapping(value = "/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //查询所有
    @GetMapping(value = "/findAll")
    public Result findAll(){
        List<CheckItem> list = checkItemService.findAll();
        // 返回页面
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
    }

    @PostMapping(value = "/add")
    public Result add(@RequestBody CheckItem checkItem){
        checkItemService.add(checkItem);
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @PostMapping(value = "/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult<CheckItem>pageResult = checkItemService.findPage(queryPageBean);

        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    @PostMapping(value = "/deleteById")
    public Result deleteById(int id){
        checkItemService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


}
