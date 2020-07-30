package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/29 21:07
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
       List<Setmeal> list= setmealService.findAll();
       list.forEach(s->s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    @PostMapping("/findDetailById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findDetailById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
