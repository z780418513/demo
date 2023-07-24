package com.hb.test;

import cn.hutool.http.useragent.UserAgentUtil;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/25
 */
public class UserAgentUtilTest {
    @Test
    public void test(){
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            String hostName = ip4.getHostName();
            System.out.println(hostName);
            System.out.println(ip4.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
