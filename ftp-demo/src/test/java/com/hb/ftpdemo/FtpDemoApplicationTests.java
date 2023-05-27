package com.hb.ftpdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.CountingInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

@SpringBootTest
@Slf4j
class FtpDemoApplicationTests {

    private FTPUtil getFTPUtil() {
        FTPUtil ftpUtil =
                FTPUtil.createSimpleFtpCli("192.168.31.201", 21, "ftpuser", "123456");
        return ftpUtil;
    }


    @Test
    void listFileNames() {
        FTPUtil ftpUtil = getFTPUtil();
        try {
            ftpUtil.connect();
            List<String> filenames = ftpUtil.listFileNames("/ccc/ddd1.xls");
            log.info("file: {}", filenames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ftpUtil.disconnect();
        }
    }

    @Test
    void uploadFile() {
        FTPUtil ftpUtil = getFTPUtil();
        try {
            ftpUtil.connect();
            ftpUtil.makeDirs("/ccc/ddd");
            ftpUtil.upload("/ccc/ddd.xls", new File("/Users/zhaochengshui/Desktop/未命名.xlsx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ftpUtil.disconnect();
        }
    }

    @Test
    void fileIsExist() {
        FTPUtil ftpUtil = getFTPUtil();
        try {
            ftpUtil.connect();
            boolean isExist = ftpUtil.fileIsExist("/ccc/ddd1.xls");
            log.info("file isExist :{}", isExist);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ftpUtil.disconnect();
        }
    }

    @Test
    void uploadProgress() throws IOException {
        FTPUtil ftpUtil = getFTPUtil();
        FileInputStream output = null;
        try {
            File file = new File("/Users/zhaochengshui/Pictures/相机胶卷/2021_10_03_12_57_IMG_2746.MOV");
            output = new FileInputStream(file);
            CountingInputStream cos = new CountingInputStream(output) {
                protected void afterRead(int n) {
                    super.afterRead(n);
                    String percent2 = getPercent2(getCount(), file.length());
                    System.out.println("percent2 = " + percent2);

                }
            };
            ftpUtil.connect();

            ftpUtil.uploadFile("2746.MOV", cos, "/accc");
            List<String> filenames = ftpUtil.listFileNames("/ccc/ddd1.xls");
            log.info("file: {}", filenames);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ftpUtil.disconnect();
            output.close();
        }


    }

    /**
     * 方式二：使用java.text.DecimalFormat实现
     * @param x
     * @param y
     * @return
     */
    public static String getPercent2(int x, long y) {
        double d1 = x * 1.0;
        double d2 = y * 1.0;
        // 设置保留几位小数， “.”后面几个零就保留几位小数，这里设置保留四位小数
        DecimalFormat decimalFormat = new DecimalFormat("##.0000%");
        return decimalFormat.format(d1 / d2);
    }

}
