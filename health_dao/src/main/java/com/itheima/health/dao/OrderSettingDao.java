package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    //根据日期查
    List<OrderSetting> getOrderSettingByMonth(Map map);


}
