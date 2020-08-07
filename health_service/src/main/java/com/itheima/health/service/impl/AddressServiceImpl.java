package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.AddressDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Address;
import com.itheima.health.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/7 13:54
 */
@Service(interfaceClass = AddressService.class)
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public PageResult<Address> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Address> page = addressDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Address> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void add(Address address) {
        addressDao.add(address);
    }

    @Override
    public List<Address> findAll() {
        return addressDao.findAll();
    }

    @Override
    public Address findById(int id) {
        return addressDao.findById(id);
    }

    @Override
    @Transactional
    public void update(Address address) {
        addressDao.update(address);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        int count = addressDao.findCountByOrder(id);
        if (count>0){
            throw new HealthException(MessageConstant.ADDRESS_IN_USE);
        }
        addressDao.deleteById(id);
    }
}
