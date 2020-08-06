package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/5 19:29
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        if (!StringUtils.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        Page<Role> page = roleDao.findByCondition(queryPageBean.getQueryString());
        PageResult<Role> pageResult = new PageResult<>(page.getTotal(),page.getResult());
        return pageResult;
    }

    @Override
    @Transactional
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.add(role);
        Integer roleId = role.getId();
        if (permissionIds!=null){
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId,permissionId);
            }
        }
        if (menuIds!=null){
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(roleId,menuId);
            }
        }
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public Role findById(int id) {
        return roleDao.findById(id);
    }

    @Override
    public List<Integer> findPermissionIdsByRoleId(int id) {
        return roleDao.findPermissionIdsByRoleId(id);
    }

    @Override
    public List<Integer> findMenuIdsByRoleId(int id) {
        return roleDao.findMenuIdsByRoleId(id);
    }

    @Override
    @Transactional
    public void update(Role role, Integer[] permissionIds, Integer[] menuIds) {
        roleDao.update(role);
        Integer roleId = role.getId();
        //删除中间表
        roleDao.deleteRolePermission(roleId);
        if (permissionIds!=null){
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId,permissionId);
            }
        }
        //删除中间表
        roleDao.deleteRoleMenu(roleId);
        if (menuIds!=null){
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(roleId,menuId);
            }
        }
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        //用户角色
        int count = roleDao.findUserByRoleId(id);
        if(count > 0){
            throw new HealthException(MessageConstant.ROLE_IN_USE);
        }
        roleDao.deleteRoleMenu(id);
        roleDao.deleteRolePermission(id);
        roleDao.deleteById(id);
    }
}
