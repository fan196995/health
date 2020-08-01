package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.ParseException;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/1 0:41
 */

@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,Object> orderInfo){
        //验证手机验证码
        String telephone = (String) orderInfo.get("telephone");
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        Jedis jedis = jedisPool.getResource();
        String codeRedis = jedis.get(key);
        if (codeRedis==null){
            return new Result(false, "请点击发送验证码");
        }
        if (!codeRedis.equals(orderInfo.get("validateCode"))){
            return new Result(false, "验证码不正确");
        }
        //清除redis中验证码
        jedis.del(key);
        //设置预约类型 微信预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submit(orderInfo);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    @PostMapping("/findById")
    public Result findById(int id){
        Map<String,Object> map =orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }
}
