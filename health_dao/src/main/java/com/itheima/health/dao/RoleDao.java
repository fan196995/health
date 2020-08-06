package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/5 19:32
 */
public interface RoleDao {

    Page<Role> findByCondition(String queryString);

    void add(Role role);

    void addRolePermission(@Param("roleId") Integer roleId, @Param("permissionId") Integer permissionId);

    void addRoleMenu(@Param("roleId") Integer roleId, @Param("menuId") Integer menuId);

    List<Role> findAll();

    Role findById(int id);

    List<Integer> findPermissionIdsByRoleId(int id);

    List<Integer> findMenuIdsByRoleId(int id);

    //修改
    void update(Role role);

    //删除角色权限中间表
    void deleteRolePermission(Integer roleId);

    //删除角色菜单中间表
    void deleteRoleMenu(Integer roleId);

    //角色用户中间表是否有值
    int findUserByRoleId(int id);

    void deleteById(int id);
}
