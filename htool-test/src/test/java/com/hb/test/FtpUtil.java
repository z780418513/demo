package com.hb.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

import java.io.IOException;
import java.math.RoundingMode;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/25
 */
public class FtpUtil {
    @Test
    public void math() throws IOException {
        //匿名登录（无需帐号密码的FTP服务器）
        // ftp://ftp:123456@192.168.31.198:2121
        // ftp://ftpuser:123456@192.168.5.7:21
        Ftp ftp = new Ftp("192.168.5.7", 21, "ftpuser", "123456", CharsetUtil.CHARSET_UTF_8, null, null, FtpMode.Active);
        //进入远程目录
        ftp.cd("/");
//上传本地文件
        boolean upload = ftp.upload("/aa", FileUtil.file("/Users/zhaochengshui/Desktop/bb.jpg"));
        System.out.println("upload = " + upload);
//下载远程文件
//        ftp.download("/opt/upload", "test.jpg", FileUtil.file("e:/test2.jpg"));

//关闭连接
        ftp.close();

    }
}
