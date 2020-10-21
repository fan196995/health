package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/29 21:07
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetmealService setmealService;

  @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        try {
            Jedis jedis = jedisPool.getResource();
            //获取setmeal_list的json格式数据
            String setmeal_list = jedis.get("setmeal_list");
            //判断
            if (setmeal_list==null||setmeal_list.length()==0){
                //没有数据从数据库查
                List<Setmeal> list = setmealService.findAll();
                 setmeal_list = JSON.toJSONString(list);
                 jedis.set("setmeal_list",setmeal_list);
                list.forEach(e->{e.setImg(QiNiuUtils.DOMAIN+e.getImg());});
                return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
            }else {
                List<Setmeal> list = JSON.parseArray(setmeal_list, Setmeal.class);
                list.forEach(e->{e.setImg(QiNiuUtils.DOMAIN+e.getImg());});
                return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
            }

        }catch (Exception e){
                return  new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    @PostMapping("/findDetailById")
    public Result findDetailById(int id) {
        try {
            Jedis jedis = jedisPool.getResource();
            String setmealDetail = "setmealDetail_" + id;
            //获取套餐详情的json格式数据
            String setmealDetail_id = jedis.get(setmealDetail);
            //判断
            if (setmealDetail_id == null || setmealDetail_id.length() == 0) {
                //从数据库查询详情
                Setmeal setmeal = setmealService.findDetailById(id);
                //转成json
                String setmeal_Detail = JSON.toJSONString(setmeal);
                //存入redis
                jedis.set(setmealDetail, setmeal_Detail);
                setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
            } else {
                Setmeal setmeal = JSON.parseObject(setmealDetail_id, Setmeal.class);
                setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
            }

        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }

    }

    @PostMapping("/findDetailById2")
    public Result findDetailById2(int id){
        Setmeal setmeal = setmealService.findDetailById2(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    @PostMapping("/findDetailById3")
    public Result findDetailById3(int id){
        Setmeal setmeal = setmealService.findDetailById3(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

}
