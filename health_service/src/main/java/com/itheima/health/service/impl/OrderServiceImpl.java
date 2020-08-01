package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/1 1:00
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    //提交预约
    @Override
    @Transactional
    public Order submit(Map<String, Object> orderInfo)  {
        //查询预约设置信息
        String orderDate = (String) orderInfo.get("orderDate");
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new HealthException("日期格式不正确");
        }

        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting==null){
            throw new HealthException("所选日期不能预约");
        }
        //预约满
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
            throw new HealthException(MessageConstant.ORDER_FULL);
        }

        //预约后已预约+1
        orderSettingDao.editReservationsByOrderDate(date);
        String telephone = ((String) orderInfo.get("telephone"));
        //是否为会员
        Member member = memberDao.findByTelephone(telephone);

        Order order = new Order();
        order.setSetmealId(Integer.valueOf((String) orderInfo.get("setmealId")));
        order.setOrderDate(date);

        if (member==null){
            //添加新会员
            member = new Member();
            member.setRegTime(new Date());
            member.setSex(((String) orderInfo.get("sex")));
            member.setPhoneNumber(telephone);
            member.setIdCard(((String) orderInfo.get("idCard")));
            member.setName(((String) orderInfo.get("name")));
            memberDao.add(member);

            //添加订单所需
            order.setMemberId(member.getId());
        }else {
            //查询所需
            order.setMemberId(member.getId());
            List<Order> orderList = orderDao.findByCondition(order);
            //有值则出现重复预约
            if (orderList!=null && orderList.size() > 0) {
                throw new HealthException(MessageConstant.HAS_ORDERED);
            }
        }
            //添加订单
            order.setOrderType(((String) orderInfo.get("orderType")));
            order.setOrderStatus(Order.ORDERSTATUS_NO);
            orderDao.add(order);
            return order;
    }

    @Override
    public Map<String, Object> findById(int id) {
        return orderDao.findById4Detail(id);
    }
}
