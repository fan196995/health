package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/28 14:34
 */
public interface SetmealDao {

    //添加
    void add(Setmeal setmeal);

    //添加中间表
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    //按条件
    Page<Setmeal> findByCondition(String queryString);

    //根据id查询
    Setmeal findById(int id);

    //查询是否有预约
    int findOrderCountBySetmealId(int id);

    //删除中间表
    void deleteSetmealCheckGroup(int id);

    //删除
    void deleteById(int id);

    void update(Setmeal setmeal);

    List<Integer> findCheckGroupIdsBySetmealId(int id);

    List<Setmeal> findAll();

}
