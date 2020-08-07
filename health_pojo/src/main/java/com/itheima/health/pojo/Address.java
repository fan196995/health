package com.itheima.health.pojo;

import java.io.Serializable;

/**
 * @author fanbo
 * @date 2020/8/7 14:02
 */
public class Address implements Serializable {

    private Integer id;//主键
    private String name;//机构名称
    private String telephone;//电话
    private String point;//经纬度
    private String address;//地址

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
