package com.hb.ftpdemo;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.ftp.session.FtpRemoteFileTemplate;

import java.io.FileOutputStream;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/11
 */
@SpringBootTest
@Slf4j
public class IntegrationFtpTest {
    // 依赖注入
    @Autowired
    private FtpRemoteFileTemplate ftpRemoteFileTemplate;


    @Test
    public void test(){
        String filePath = "未命名.txt";

        // 判断是否存在
//        boolean exists = ftpRemoteFileTemplate.exists(filePath);

        // 下载文件
        ftpRemoteFileTemplate.get(filePath, (inputStream -> {
            try (FileOutputStream fos = new FileOutputStream("/Users/zhaochengshui/Desktop/" + filePath)) {
                IOUtils.copy(inputStream, fos);
            }
        }));


    }
}
