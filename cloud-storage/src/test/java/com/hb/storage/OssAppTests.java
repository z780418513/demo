package com.hb.storage;

import com.hb.storage.config.OssConfig;
import com.hb.storage.service.AliyunOssService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.*;

@SpringBootTest(classes = OssConfig.class)
class OssAppTests {
    @Resource
    private AliyunOssService aliyunOssService;


    @Test
    void contextLoads() {
        File file = new File("/Users/zhaochengshui/code/hb-core/oss-spring-boot-starter/src/main/java/com/hb/oss/config/OssConfig.java");
        System.out.println(file.getName());
        String path = file.getPath();
        System.out.println(path);
    }

    @Test
    void aliUpload() {
        String file = aliyunOssService.upLoadFile("/Users/zhaochengshui/code/hb-core/oss-spring-boot-starter/src/main/java/com/hb/oss/config/OssConfig.java", null);
        System.out.println(file);
    }

    @Test
    void delete() {
        boolean b = aliyunOssService.deleteFile("2022/09/23/7d9ecf11-232d-412b-aa74-520700f19991OssConfig.java", null);
        System.out.println(b);
    }

    @Test
    void down() throws IOException {
        InputStream inputStream = aliyunOssService.downFile("2022/09/23/ab1ded99-1bd8-408a-a3b2-981c8ac2de78OssConfig.java", null);
        System.out.println(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("\n" + line);
        }
    }


    @Test
    void deleteBuket() throws IOException {
        boolean b = aliyunOssService.deleteBuket("simple-bucket-hb");
        System.out.println(b);

    }

    @Test
    void upload() throws IOException {
        File file = new File("/Users/zhaochengshui/code/hb-core/oss-spring-boot-starter/HELP.md");
        String s = aliyunOssService.upLoadFile(new FileInputStream(file), file.getName(), null);
        System.out.println(s);

    }


}
