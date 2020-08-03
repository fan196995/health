package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/3 17:15
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReport() {
        Map<String,Object> reportData = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //周一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(date));
        //周日
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(date));
        //1日
        String firstDay = sdf.format(DateUtils.getFirstDay4ThisMonth());
        //31日
        String lastDay = sdf.format(DateUtils.getLastDayOfThisMonth());

        String reportDate = sdf.format(date);

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //总会员数
        Integer totalMember = memberDao.findMemberTotalCount();
        //本周新增会员数   本周一到现在
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //本月新增会员     本月第一天到现在
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay);

        //订单
        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //本周预约数  周一到周日
        Integer  thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //本月预约数 本月第一天到最后一天
        Integer thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDay,lastDay);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay);

        //热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;

    }
}
