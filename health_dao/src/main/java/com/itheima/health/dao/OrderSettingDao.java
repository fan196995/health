package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;

/**
 * @author fanbo
 * @date 2020/7/29 15:10
 */
public interface OrderSettingDao {

    //查询日期
    OrderSetting findByOrderDate(Date orderDate);

    //更新
    void update(OrderSetting orderSetting);

    //添加
    void add(OrderSetting orderSetting);

}
