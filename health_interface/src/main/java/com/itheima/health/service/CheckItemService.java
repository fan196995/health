package com.itheima.health.service;


import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

     // 查询所有
    List<CheckItem> findAll();

    //添加检查项
    void add(CheckItem checkItem);

    //分页
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    //删除检查项
    void deleteById(int id) throws HealthException;
}
