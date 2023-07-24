package com.hb.ftpdemo;

import org.apache.commons.net.SocketClient;
import org.apache.commons.net.ftp.FTPClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/7/9
 */
public class FtpNioTest {


    public static void main(String[] args) throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("localhost",2021);
        ftpClient.login("ftpuser","123456");
        OutputStream outputStream = ftpClient.storeFileStream("/1.txt");

        System.out.println(outputStream);
//        outputStream= FileOutputStream(outputStream);
//        FileOutputStream fileOutputStream = new FileOutputStream("/Users/zhaochengshui/Desktop/未命名.txt");
//        // 分配15个字节的buffer
//        ByteBuffer buffer = ByteBuffer.allocate(15);
//        // channel
//        FileChannel channel = outputStream.getChannel();

    }
}
