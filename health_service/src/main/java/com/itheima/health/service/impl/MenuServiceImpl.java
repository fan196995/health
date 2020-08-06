package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/6 12:02
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> findAll() {
        return menuDao.findAll();
    }
}
