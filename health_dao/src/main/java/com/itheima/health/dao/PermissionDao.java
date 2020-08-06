package com.itheima.health.dao;

import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/6 12:01
 */
public interface PermissionDao {
    List<Permission> findAll();

}
