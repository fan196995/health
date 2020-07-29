package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/7/28 14:30
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.add(setmeal);
        //设置中间表数据
        if (checkgroupIds!=null){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(),checkgroupId);
            }
        }
    }

    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Setmeal> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        //是否被预约
        int count = setmealDao.findOrderCountBySetmealId(id);
        if (count>0){
            throw new HealthException(MessageConstant.SETMEAL_IN_USE);
        }
        setmealDao.deleteSetmealCheckGroup(id);
        setmealDao.deleteById(id);
    }

    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        setmealDao.update(setmeal);
        //先删除再添加
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        if (checkgroupIds!=null){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(),checkgroupId);
            }
        }
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }
}
