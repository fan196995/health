package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.naming.NamingException;
import java.util.concurrent.TimeUnit;

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
        if (!StringUtils.isEmpty(codeRedis)){
            return new Result(false, "验证码已经发送过了，请注意查收!");
        }else {
            //生成6位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
            } catch (ClientException e) {
                e.printStackTrace();
                // 发送失败
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
            //Setex 命令为指定的 key 设置值及其过期时间
            jedis.setex(key,15*60,code+"");
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }
    }

    //登录
    @PostMapping("/send4Login")
    public Result send4Login(String telephone) throws ClientException {
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String codeRedis = jedis.get(key);
        if (codeRedis==null){
            Integer code = ValidateCodeUtils.generateValidateCode(6);
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code+"");
                jedis.setex(key,15*60,code+"");
                return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
            }
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }

}
