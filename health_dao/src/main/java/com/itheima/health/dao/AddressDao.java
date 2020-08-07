package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Address;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/7 13:54
 */
public interface AddressDao {
    Page<Address> findByCondition(String queryString);

    void add(Address address);

    List<Address> findAll();

    Address findById(int id);

    void update(Address address);

    int findCountByOrder(int id);

    void deleteById(int id);
}
