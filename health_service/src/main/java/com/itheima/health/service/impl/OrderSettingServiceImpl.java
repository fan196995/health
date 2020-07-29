package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/29 15:08
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            //查询是否有该日期
            OrderSetting order=orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if (order!=null){
                if (order.getReservations()>order.getNumber()){
                    throw new HealthException(orderSetting.getOrderDate()+"已预约数不能超过可预约数");
                }
                orderSettingDao.update(orderSetting);
            }else {
                orderSettingDao.add(orderSetting);
            }
        }
    }
}
