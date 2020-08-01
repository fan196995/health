package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author fanbo
 * @date 2020/7/28 14:28
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    //上传图片
    @PostMapping(value = "/upload")
    public Result upload(MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();
        //图片后缀
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //文件名
        String imageName = UUID.randomUUID().toString() + substring;

        Jedis jedis = jedisPool.getResource();
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),imageName);
            // 保存所有上传的图片到redis集合中
            //Sadd 命令将一个或多个成员元素加入到集合中，已经存在于集合的成员元素将被忽略
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES,imageName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        } finally {
            if (jedis!=null){
                // 返回jedis连接池
                jedis.close();
            }
        }
        Map<String,String> dataMap = new HashMap<>();
        dataMap.put("imgName",imageName);
        dataMap.put("domain",QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,dataMap);
    }

    @PostMapping(value ="/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        setmealService.add(setmeal,checkgroupIds);
        //存入另一个key
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedis.close();
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping(value = "/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        Setmeal old = setmealService.findById(setmeal.getId());
        setmealService.update(setmeal,checkgroupIds);
        //srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。
        //先删除再添加
        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,old.getImg());
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedis.close();
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    @PostMapping(value = "/deleteById")
    public Result deleteById(int id){
        Setmeal setmeal =setmealService.findById(id);
        setmealService.deleteById(id);
        Jedis jedis =jedisPool.getResource();

        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        jedis.close();
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @PostMapping(value = "/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        String img = setmeal.getImg();
        Map<String,Object> map = new HashMap<>();
        //formData
        map.put("setmeal",setmeal);
        //imageUrl
        map.put("imageUrl",QiNiuUtils.DOMAIN+img);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,map);
    }

    @GetMapping(value = "/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> checkGroupIds = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
    }
}
