package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Address;
import com.itheima.health.service.AddressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/7 20:40
 */

@RestController
@RequestMapping(value = "/address")
public class AddressMobileController {

    @Reference
    private AddressService addressService;

    @GetMapping(value = "/findAllMobile")
    public Result findAllMobile(){
        List<Address> addressList = addressService.findAll();
        return new Result(true,MessageConstant.QUERY_ADDRESS_SUCCESS,addressList);
    }


}
