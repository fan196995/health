package com.itheima.health.job;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author fanbo
 * @date 2020/7/28 19:36
 */
@Component
public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
        Jedis jedis = jedisPool.getResource();
        //垃圾图片=七牛云所有图片-数据库图片
        // Sdiff 命令返回第一个集合与其他集合之间的差异，也可以认为说第一个集合中独有的元素
        Set<String> sdiff = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        //set转换为string数组
        QiNiuUtils.removeFiles(sdiff.toArray(new String[]{}));

        // DEL 命令用于删除已存在的键。不存在的 key 会被忽略。
        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES,RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println("===================="+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
