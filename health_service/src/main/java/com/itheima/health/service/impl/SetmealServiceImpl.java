package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanbo
 * @date 2020/7/28 14:30
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${out_put_path}")//从属性文件读取输出目录的路径
    private String outputpath ;

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
        //重新生成静态页面
        generateMobileStaticHtml();
    }

    //生成静态页面
    private void generateMobileStaticHtml(){
        try {
            //套餐列表
            generateSetmealListHtml();
            //套餐详情
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //套餐详情
    private void generateSetmealDetailHtml() throws Exception {
        List<Setmeal> setmealList = setmealDao.findAll();
        for (Setmeal setmeal : setmealList) {
            //详情
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());
            //设置图片路径
            setmealDetail.setImg(QiNiuUtils.DOMAIN+setmealDetail.getImg());
            //生成静态页
            generateDetailHtml(setmealDetail);
        }

    }
    //套餐详情静态页
    private void generateDetailHtml(Setmeal setmealDetail) throws Exception {
        //获取模版
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmeal",setmealDetail);
        //解决乱码 utf-8
        File setmealDetailFile = new File(outputpath, "setmeal_detail_"+setmealDetail.getId()+".html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealDetailFile),"utf-8")));
    }

    //套餐列表
    private void generateSetmealListHtml() throws Exception {
        //获取模版
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");
        //数据
        List<Setmeal> setmealList = setmealDao.findAll();
        setmealList.forEach(s->s.setImg(QiNiuUtils.DOMAIN+s.getImg()));
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("setmealList",setmealList);
        //解决乱码 utf-8
        File setmealListFile = new File(outputpath, "m_setmeal.html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealListFile),"utf-8")));
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
        //重新生成静态页面
        generateMobileStaticHtml();
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
        //重新生成静态页面
        generateMobileStaticHtml();
    }

    @Override
    public List<Integer> findCheckGroupIdsBySetmealId(int id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }

    @Override
    public Setmeal findDetailById3(int id) {
        Setmeal setmeal = setmealDao.findDetailById3(id);
        //检查组列表
        List<CheckGroup> checkGroupList= setmealDao.findCheckGroupListBySetmealId(setmeal.getId());
        for (CheckGroup checkGroup : checkGroupList) {
            //检查项列表
            List<CheckItem> checkItemList = setmealDao.findCheckItemByCheckGroupId(checkGroup.getId());
            checkGroup.setCheckItems(checkItemList);
        }
        setmeal.setCheckGroups(checkGroupList);
        return setmeal;
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

}
