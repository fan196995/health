package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
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

    //前台
    Setmeal findDetailById(int id);

    Setmeal findDetailById2(int id);

    Setmeal findDetailById3(int id);

    //根据套餐id查检查组
    List<CheckGroup> findCheckGroupListBySetmealId(int id);

    //根据检查组id查检查项
    List<CheckItem> findCheckItemByCheckGroupId(int id);

}
