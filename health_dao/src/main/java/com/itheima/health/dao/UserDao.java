package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @author fanbo
 * @date 2020/8/2 15:00
 */
public interface UserDao {

    User findUserByUsername(String username);

    void add(User user);

}
