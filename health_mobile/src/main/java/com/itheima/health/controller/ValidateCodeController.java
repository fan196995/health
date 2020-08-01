package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author fanbo
 * @date 2020/7/31 23:55
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        Jedis jedis = jedisPool.getResource();
        //redis中验证码
        String key = RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone;
        String codeRedis = jedis.get(key);
        if (codeRedis==null){
            //生成6位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
                //Setex 命令为指定的 key 设置值及其过期时间
                jedis.setex(key,15*60,code+"");
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                // 发送失败
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }
}
