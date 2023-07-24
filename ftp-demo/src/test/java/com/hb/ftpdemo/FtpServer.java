package com.hb.ftpdemo;

import cn.hutool.extra.ftp.SimpleFtpServer;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.junit.jupiter.api.Test;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/11
 */
public class FtpServer {
    @Test
    public void start() {
        BaseUser user = new BaseUser();
        user.setName("ftpuser");
        user.setPassword("123456");
        user.setHomeDirectory("/Users/zhaochengshui/data/docker_data/ftp");
        SimpleFtpServer.create()
                .addUser(user)
                .setPort(2021)
                .start();
        while (true);
    }
}
