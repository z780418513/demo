package com.hb.test;

import cn.hutool.core.io.IoUtil;
import org.junit.Test;
import sun.nio.ch.FileChannelImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class IoUtilTest {
    @Test
    public void test() throws FileNotFoundException {
        long start = System.currentTimeMillis();
        String file = "/Users/hanbaolaoba/Downloads/【于老师说历史】《长安三万里》值得看吗？听历史教授来说说 - 001 - 【于老师说历史】《长安三万里》值得看吗？听历史教授来说说.mp4";
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream("/Users/hanbaolaoba/Desktop/1.mp4");
        IoUtil.copy(fileInputStream, fileOutputStream, 1024, null);
        System.out.println("BIO :" + (System.currentTimeMillis() - start));


        long  start2 = System.currentTimeMillis();
        FileInputStream fis2 = new FileInputStream(file);
        FileOutputStream fos2 = new FileOutputStream("/Users/hanbaolaoba/Desktop/2.mp4");
        IoUtil.copyByNIO(fis2, fos2, 1024, null);
        System.out.println("NIO :" + (System.currentTimeMillis() - start2));
    }
}
