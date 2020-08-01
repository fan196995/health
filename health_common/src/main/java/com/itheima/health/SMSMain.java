package com.itheima.health;

import com.itheima.health.utils.SMSUtils;

/**
 * @author fanbo
 * @date 2020/8/1 11:33
 */
public class SMSMain {
    public static void main(String[] args) throws Exception {
        SMSUtils.sendShortMessage("SMS_174986903","13873008724","1234");
    }
}