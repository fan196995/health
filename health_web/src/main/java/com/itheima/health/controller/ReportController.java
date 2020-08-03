package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetmealService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fanbo
 * @date 2020/8/3 15:21
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    //去年一年的会员数量
    @GetMapping(value = "/getMemberReport")
    public Result getMemberReport(){
        //months
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //过去一年的数据
        calendar.add(Calendar.MONTH,-12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        //12个月数据
        for (int i = 0; i <12; i++) {
            calendar.add(Calendar.MONTH,1);
            //日期
            Date date = calendar.getTime();
            //展示年-月
            months.add(sdf.format(date));
        }

        //memberCount
        List<Integer> memberCount = memberService.getMemberReportCount(months);

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }

    //套餐预约饼状图
    @GetMapping(value = "/getSetmealReport")
    public Result getSetmealReport(){
        List<String> setmealNames = new ArrayList<>();
        List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
        if (setmealCount!=null){
            for (Map<String, Object> map : setmealCount) {
//                向setmealNames中添加name
                setmealNames.add((String) map.get("name"));
            }
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }
}
