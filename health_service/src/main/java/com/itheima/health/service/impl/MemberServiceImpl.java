package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fanbo
 * @date 2020/8/1 18:23
 */
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;


    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> getMemberReportCount(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        if (months!=null){
            for (String month : months) {
                Integer count =memberDao.findMemberCountBeforeDate(month+ "-31");
                memberCount.add(count);
            }
        }
        return memberCount;
    }
}
