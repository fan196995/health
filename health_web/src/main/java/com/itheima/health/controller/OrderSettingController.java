package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/7/29 15:06
 */
@RestController
@RequestMapping(value = "/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping(value = "/upload")
    public Result upload(MultipartFile excelFile){
        List<String[]> strings = null;
        try {
            strings = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettingList = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            Date date = null;
            OrderSetting orderSetting = null;
            int number =0;
            for (String[] string : strings) {
                date = format.parse(string[0]);
                number = Integer.parseInt(string[1]);
                orderSetting = new OrderSetting(date,number);
                orderSettingList.add(orderSetting);
            }
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @GetMapping(value = "/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){
        try {
            //TODO List<Map> 使用原因
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    //TODO BUG待解决
    @PostMapping(value = "/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
