package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

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

}
