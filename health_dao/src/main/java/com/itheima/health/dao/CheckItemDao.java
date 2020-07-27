package com.itheima.health.dao;


import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {

     //查询所有
    List<CheckItem> findAll();

    //添加检查项
    void add(CheckItem checkItem);

    //分页
    Page<CheckItem> findByCondition(String queryString);

    //删除检查项
    void deleteById(int id);

    //查询有无检查组
    int findCountByCheckItemId(int id);
}
