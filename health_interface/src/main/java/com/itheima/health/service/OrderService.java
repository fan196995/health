package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/1 1:01
 */
public interface OrderService {

    //提交预约
    Order submit(Map<String, Object> orderInfo) throws HealthException;

    Map<String, Object> findById(int id);
}
