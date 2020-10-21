package com.itheima.health.pojo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/9 9:00
 */
public class Tree<T> {

    private Integer id;           // 节点id
    private Integer parentId;    // 父节点
    private String name;         // 节点名称 ,返回给前台的是一个装有TreeUtils的集合的数据，所以在前台显示数据的时候，el-tree的lable的名字的和这个一样
    private List<Tree> childList;  // 父节点中存放子节点的集合
    private T data;              //  节点数据

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tree> getChildList() {
        return childList;
    }

    public void setChildList(List<Tree> childList) {
        this.childList = childList;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    /**
     * 通过反射得到的数据类型的也是不一定的，所以这里我们返回值为Object
     * Object是无法直接转为Integer,现将Object转为String，然后再将String转为Integer
     * @param item
     * @param fileName
     * @return
     */
    public static Object  getFileValue(Object item,String fileName) throws Exception {
        Class<?> aClass = item.getClass();
        Field file = aClass.getDeclaredField(fileName); // 得到所有字段包括私有字段
        file.setAccessible(true); // 取消访问限制
        return file.get(item);    // 这里就体现出反射的意思了，我们通常都是通过对象拿到字段，这里是通过字段，将类的字节码对象为参数传入，来得到值
    }
}
