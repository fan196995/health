package com.itheima.health.service;

import com.itheima.health.pojo.Member;

import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/1 18:23
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> getMemberReportCount(List<String> months);
}
