package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/29 15:08
 */
public interface OrderSettingService {

    void add(List<OrderSetting> orderSettingList) throws HealthException;;


}
