package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/7/29 15:08
 */
public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList) throws HealthException;

    //根据日期查
    List<Map<String,Integer>> getOrderSettingByMonth(String month);

    //修改最大可预约人数
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;

}
