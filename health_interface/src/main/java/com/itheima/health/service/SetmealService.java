package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/28 14:29
 */
public interface SetmealService {

    //添加套餐
    void add(Setmeal setmeal, Integer[] checkgroupIds);

    //分页
    PageResult<Setmeal> findPage(QueryPageBean queryPageBean);

    Setmeal findById(int id);

    void deleteById(int id) throws HealthException;

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    //查询中间表
    List<Integer> findCheckGroupIdsBySetmealId(int id);

    List<Setmeal> findAll();

    //前台
    Setmeal findDetailById(int id);

    Setmeal findDetailById2(int id);

    Setmeal findDetailById3(int id);
}
