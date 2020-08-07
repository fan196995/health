package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Address;
import com.itheima.health.service.AddressService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/7 13:53
 */

@RestController
@RequestMapping(value = "/address")
public class AddressController {

    @Reference
    public AddressService addressService;

    @PostMapping(value = "/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Address> pageResult = addressService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_ADDRESS_SUCCESS,pageResult);
    }

    @PostMapping(value = "/add")
    public Result add(@RequestBody Address address){
        addressService.add(address);
        return new Result(true,MessageConstant.ADD_ADDRESS_SUCCESS);
    }

    @GetMapping(value = "/findAll")
    public Result findAll(){
        List<Address> addressList =addressService.findAll();
        return new Result(true,MessageConstant.QUERY_ADDRESSLIST_SUCCESS,addressList);
    }

    @GetMapping(value = "/findById")
    public Result findById(int id){
        Address address = addressService.findById(id);
        return new Result(true,MessageConstant.QUERY_ADDRESS_SUCCESS,address);
    }


    @PostMapping (value = "/update")
    public Result update(@RequestBody Address address){
        addressService.update(address);
        return new Result(true,MessageConstant.EDIT_ADDRESS_SUCCESS);
    }

    @PostMapping(value = "/deleteById")
    public Result deleteById(int id){
        addressService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_ADDRESS_SUCCESS);
    }

}
