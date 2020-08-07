package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Address;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/7 13:54
 */
public interface AddressService {
    PageResult<Address> findPage(QueryPageBean queryPageBean);

    void add(Address address);

    List<Address> findAll();

    Address findById(int id);

    void update(Address address);

    void deleteById(int id) throws HealthException;
}
