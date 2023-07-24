package com.hb.juc.test;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/11
 */
public class OutputStreamWirteTest {
    @Test
    public void test() {
        String s = "hello world";
        for (int i = 0; i < 3; i++) {
            String substring = s.substring(i * 2, (i + 1) * 2);
            int finalI = i;
            new Thread(
                    () -> {

                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(new File("/Users/zhaochengshui/Desktop/未命名 4.txt"));
                            FileChannel channel = outputStream.getChannel();
                            byte[] bytes = new byte[1024];
                            outputStream.write(s.getBytes(), finalI * 2, 2);
                            outputStream.flush();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
            ).start();
        }
    }
}
