package com.hb.storage;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/6/13
 */
@Slf4j
public class ObsTest {

    public ObsClient getObsClient() {
        ObsClient obsClient = new ObsClient("HAMEL2CAQSTJ5DD3AIJE", "bySZ2z1QJE2AKZ8pKRuXkIuSYNZXCnnNVvpbcnoq", "obs.cn-east-3.myhuaweicloud.com");
        return obsClient;
    }

    @Test
    public void test() throws FileNotFoundException, InterruptedException {
        ObsClient obsClient = getObsClient();
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        FileInputStream inputStream = new FileInputStream(new File("/Users/zhaochengshui/Downloads/【彭于晏、陈冠希、吴彦祖和我】这个时期的彦祖，连我都要暂避锋芒！ - 001 - studio_video_1685696346045.mp4"));
        putObjectRequest.setObjectKey("/aaa/1.mp4");
        putObjectRequest.setBucketName("ze-sja");
        putObjectRequest.setInput(inputStream);


        new Thread(() -> {
            PutObjectResult putObjectResult = obsClient.putObject(putObjectRequest);
            System.out.println("putObjectResult = " + putObjectResult);
        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                System.out.println("++++++++++++++ = " + inputStream);
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();

//        new Thread(()->{
//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                obsClient.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();
//
        TimeUnit.SECONDS.sleep(20);
//    }
    }

    @Test
    public void test2() throws IOException, InterruptedException {
        ObsClient obsClient = getObsClient();


        PutObjectRequest putObjectRequest = new PutObjectRequest();
        FileInputStream inputStream = new FileInputStream(new File("/Users/zhaochengshui/Downloads/【彭于晏、陈冠希、吴彦祖和我】这个时期的彦祖，连我都要暂避锋芒！ - 001 - studio_video_1685696346045.mp4"));
        putObjectRequest.setBucketName("ze-sja");
        putObjectRequest.setObjectKey("/aaa/" + 1 + ".mp4");
        putObjectRequest.setInput(inputStream);


        PutObjectRequest putObjectRequest2 = new PutObjectRequest();
        FileInputStream inputStream2 = new FileInputStream(new File("/Users/zhaochengshui/Downloads/【彭于晏、陈冠希、吴彦祖和我】这个时期的彦祖，连我都要暂避锋芒！ - 001 - studio_video_1685696346045.mp4"));
        putObjectRequest2.setBucketName("ze-sja");
        putObjectRequest2.setObjectKey("/aaa/" + 2 + ".mp4");
        putObjectRequest2.setInput(inputStream2);


        new Thread(() -> {
            PutObjectResult putObjectResult = obsClient.putObject(putObjectRequest);
            log.info("======> {}", putObjectResult);
        }).start();

        new Thread(() -> {
            PutObjectResult putObjectResult2 = obsClient.putObject(putObjectRequest2);
            log.info("2======> {}", putObjectResult2);
        }).start();

//        new Thread(()->{
//            try {
//                TimeUnit.SECONDS.sleep(20);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                inputStream.close();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }).start();

        new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                putObjectRequest2.getInput().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
        TimeUnit.SECONDS.sleep(30);
    }
}
