package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * @author fanbo
 * @date 2020/8/2 14:58
 */
public interface UserService {

    User findUserByUsername(String username);

    void add(User user);
}
