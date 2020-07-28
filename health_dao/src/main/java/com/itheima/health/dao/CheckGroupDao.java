package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/27 20:32
 */
public interface CheckGroupDao {

    //添加checkGroup
    void add(CheckGroup checkGroup);

    //添加中间表数据
    void addCheckGroupCheckItem(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    //删除中间表数据
    void deleteCheckGroupCheckItem(int id);

    //删除checkGroup
    void deleteById(int id);

    //根据条件查
    Page<CheckGroup> findByCondition(String queryString);

    //查询有无检查组关系
    int findSetmealCountByCheckGroupId(int id);

    //根据id查
    CheckGroup findById(int id);

    //修改
    void update(CheckGroup checkGroup);

    //编辑里根据所选检查组id查询出对应的检查项列表
    List<Integer> findCheckItemIdsByCheckGroupId(int id);

}
