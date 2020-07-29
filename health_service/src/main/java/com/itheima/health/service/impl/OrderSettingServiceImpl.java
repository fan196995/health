package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";//2020-05-1
        String dateEnd = date + "-31";//2020-05-31
        Map map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        List<OrderSetting> orderSettingList =orderSettingDao.getOrderSettingByMonth(map);
        List<Map> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            //orderSetting.getOrderDate().getDate() 返回月份的某一天
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;
    }

    @Override
    @Transactional
    public void editNumberByDate(OrderSetting orderSetting) {
        //查询是否有该日期
        orderSetting=orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        int reservations = orderSetting.getReservations();
        int number = orderSetting.getNumber();
        if (orderSetting!=null){
            if (reservations>number){
                throw new HealthException("最大预约数不能低于已预约数");
            }
            orderSettingDao.editNumberByDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}

