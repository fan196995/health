package com.itheima.health.service;

import com.itheima.health.pojo.Member;

/**
 * @author fanbo
 * @date 2020/8/1 18:23
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);
}
