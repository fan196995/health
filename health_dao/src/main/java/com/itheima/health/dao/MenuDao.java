package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/6 12:02
 */
public interface MenuDao {

    List<Menu> findAll();

    List<Menu> findByUsername();
}
