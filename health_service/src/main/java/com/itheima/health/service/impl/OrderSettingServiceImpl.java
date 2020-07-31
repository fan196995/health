package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
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
        if (orderSettingList!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (OrderSetting orderSetting : orderSettingList) {
                //查询是否有该日期
                OrderSetting order = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
                if (order!=null){
                    //应注意order和orderSetting的区别！！！
                    if (order.getReservations()> orderSetting.getNumber()){
                        throw new HealthException(sdf.format(orderSetting.getOrderDate())+"已预约数不能超过可预约数");
                    }
                    orderSettingDao.update(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map<String,Integer>> getOrderSettingByMonth(String date) {
        String startDate = date + "-01"; // 2020-07-01
        String endDate = date + "-31"; // 2020-07-31
        List<Map<String,Integer>> monthDatas = orderSettingDao.getOrderSettingByMonth(startDate, endDate);
        return monthDatas;

/*        //dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";//2020-05-1
        String dateEnd = date + "-31";//2020-05-31
        Map map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);

        List<OrderSetting> orderSettingList =orderSettingDao.getOrderSettingByMonth(map);
        List<Map<String,Integer>> data = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map orderSettingMap = new HashMap();
            //orderSetting.getOrderDate().getDate() 返回月份的某一天
            orderSettingMap.put("date",orderSetting.getOrderDate().getDate());
            orderSettingMap.put("number",orderSetting.getNumber());
            orderSettingMap.put("reservations",orderSetting.getReservations());
            data.add(orderSettingMap);
        }
        return data;*/
    }

    @Override
    @Transactional
    public void editNumberByDate(OrderSetting orderSetting) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询是否有该日期
        OrderSetting order=orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if (order!=null){
            if (order.getReservations()>orderSetting.getNumber()){
                throw new HealthException(sdf.format(orderSetting.getOrderDate())+"设置的最大预约数不能低于已预约数");
            }
            orderSettingDao.update(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}

