package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/8/1 18:09
 */
@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping(value = "/check")
    public Result check(@RequestBody Map<String,String> loginInfo, HttpServletResponse response){
        String telephone = loginInfo.get("telephone");

        String key = RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone;
        Jedis jedis = jedisPool.getResource();
        String codeRedis = jedis.get(key);

        if (codeRedis==null){
            return new Result(false, "请点击发送验证码");
        }
        if (!codeRedis.equals(loginInfo.get("validateCode"))){
            return new Result(false, "验证码不正确");
        }
        jedis.del(key);

        //是否为会员
        Member member = memberService.findByTelephone(telephone);
        if (member==null){
            //不是会员新建
            member = new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            member.setRemark("手机会员注册");
            memberService.add(member);
        }

        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setMaxAge(30*24*60*60);//一个月
        cookie.setPath("/");  //所有路径
        response.addCookie(cookie);

        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
