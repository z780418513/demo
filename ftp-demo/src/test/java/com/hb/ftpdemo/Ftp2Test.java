package com.hb.ftpdemo;

import ftp2.FtpClientFactory;
import ftp2.FtpUtil;
import org.junit.Test;

public class Ftp2Test {
    @Test
    public void uploadByNio(){
        // ftp://ftpuser:d9rQyyedLqXiKCUz@172.19.0.8:21
        FtpClientFactory factory = new FtpClientFactory("172.19.0.8", 21, "ftpuser", "d9rQyyedLqXiKCUz");
        FtpUtil ftpUtil = new FtpUtil(factory);
        // /Users/hanbaolaoba/Downloads/【于老师说历史】《长安三万里》值得看吗？听历史教授来说说 - 001 - 【于老师说历史】《长安三万里》值得看吗？听历史教授来说说.mp4
        String localFile = "/Users/hanbaolaoba/Downloads/【于老师说历史】《长安三万里》值得看吗？听历史教授来说说 - 001 - 【于老师说历史】《长安三万里》值得看吗？听历史教授来说说.mp4";
        long start = System.currentTimeMillis();
        ftpUtil.upload("/b/111.mp4",localFile,1024,null);
        System.out.println("Bio: " + (System.currentTimeMillis() - start));

        FtpUtil ftpUtil2 = new FtpUtil(factory);
        long start2 = System.currentTimeMillis();
        ftpUtil2.uploadByNio("/b/123.mp4",localFile,1024,null);
        System.out.println("Nio: " + (System.currentTimeMillis() - start2));
    }
}
