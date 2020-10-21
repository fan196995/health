package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/5 19:29
 */
public interface RoleService {

    PageResult<Role> findPage(QueryPageBean queryPageBean);

    void add(Role role, Integer[] permissionIds, Integer[] menuIds);

    List<Role> findAll();

    Role findById(int id);

    //权限
    List<Integer> findPermissionIdsByRoleId(int id);

    //菜单
    List<Integer> findMenuIdsByRoleId(int id);

    //修改
    void update(Role role, Integer[] permissionIds, Integer[] menuIds);

    void deleteById(int id) throws HealthException;

    List<Integer> findRoleMenuIds(int id);
}
