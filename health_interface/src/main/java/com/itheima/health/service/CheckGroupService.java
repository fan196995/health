package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/27 20:24
 */
public interface CheckGroupService {

    //新增
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    //分页
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    //删除
    void deleteById(int id) throws HealthException;

    //根据id查
    CheckGroup findById(int id);

    //修改
    void update(CheckGroup checkGroup,Integer[] checkitemIds);

    //编辑里根据所选检查组id查询出对应的检查项列表
    List<Integer> findCheckItemIdsByCheckGroupId(int id);
}
