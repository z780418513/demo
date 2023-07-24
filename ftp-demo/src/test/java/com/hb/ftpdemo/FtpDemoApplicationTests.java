//package com.hb.ftpdemo;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.input.CountingInputStream;
//import org.apache.commons.net.ftp.FTPClient;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.HashMap;
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.TimeUnit;
//
//@SpringBootTest
//@Slf4j
//class FtpDemoApplicationTests {
//
//    private FTPUtil getFTPUtil() {
//        FTPClient ftpClient = new FTPClient();
//        FTPUtil ftpUtil =
////                FTPUtil.createSimpleFtpCli("172.19.0.8", 21, "ftpuser", "d9rQyyedLqXiKCUz");
////                FTPUtil.createSimpleFtpCli("127.0.0.1", 2021, "ftpuser", "123456");
//                FTPUtil.createSimpleFtpCli("192.168.31.201", 21, "ftpuser", "123456");
//        return ftpUtil;
//    }
//
//
//    @Test
//    void listFileNames() {
//        FTPUtil ftpUtil = getFTPUtil();
//        try {
//            ftpUtil.connect();
//            List<String> filenames = ftpUtil.listFileNames("/test/movice/222/20151231030245752.jpg");
//            boolean fileIsExist = ftpUtil.fileIsExist("/test/movice/222/20151231030245752.jpg");
//            log.info("file: {}", filenames);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            ftpUtil.disconnect();
//        }
//    }
//
//    @Test
//    void uploadFile() throws Exception {
//
//        CompletableFuture<Object> future = CompletableFuture.supplyAsync(() -> {
//            FTPUtil ftpUtil = getFTPUtil();
//            try {
//                ftpUtil.connect();
//                ftpUtil.makeDirs("/mmm/ccc");
//                ftpUtil.upload("/mmm/ccc/aa.data", new File("/Users/zhaochengshui/Desktop/big_file.data"));
////                ftpUtil.makeDirs("/ccc/ddd");
////                ftpUtil.uploadFile("1.jpg", Files.newInputStream(Paths.get("/Users/zhaochengshui/Desktop/big_file.data")), "/ccc/ddd");
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            } finally {
//                ftpUtil.disconnect();
//            }
//            System.out.println("上传结束");
//            return null;
//        });
//
////        CompletableFuture<Object> async = CompletableFuture.supplyAsync(() -> {
////            int i = 0;
////            while (!future.isDone()) {
////                System.out.println("监听上传");
////                try {
////                    TimeUnit.SECONDS.sleep(1);
////                } catch (InterruptedException e) {
////                    throw new RuntimeException(e);
////                }
////                i++;
////                if (i >= 5) {
////                    future.cancel(true);
////                    System.out.println("取消任务");
////                    break;
////                }
////            }
////            return null;
////        });
//        TimeUnit.SECONDS.sleep(100);
//        future.whenComplete((value, throwable) -> {
//            System.out.println("上传结束");
//        });
//
//    }
//
//    @Test
//    void fileIsExist() {
//        FTPUtil ftpUtil = getFTPUtil();
//        try {
//            ftpUtil.connect();
//            boolean isExist = ftpUtil.fileIsExist("/ccc/ddd1.xls");
//            log.info("file isExist :{}", isExist);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            ftpUtil.disconnect();
//        }
//    }
//
//    @Test
//    void uploadProgress() throws IOException {
//        FTPUtil ftpUtil = getFTPUtil();
//        FileInputStream output = null;
//        try {
//            File file = new File("/Users/zhaochengshui/Pictures/相机胶卷/2021_10_03_12_57_IMG_2746.MOV");
//            output = new FileInputStream(file);
//            CountingInputStream cos = new CountingInputStream(output) {
//                protected void afterRead(int n) {
//                    super.afterRead(n);
//                    String percent2 = getPercent2(getCount(), file.length());
//                    System.out.println("percent2 = " + percent2);
//
//                }
//            };
//            ftpUtil.connect();
//
//            ftpUtil.uploadFile("2746.MOV", cos, "/accc");
//            List<String> filenames = ftpUtil.listFileNames("/ccc/ddd1.xls");
//            log.info("file: {}", filenames);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } finally {
//            ftpUtil.disconnect();
//            output.close();
//        }
//
//
//    }
//
//    @Test
//    public void test() {
//        FTPUtil ftpUtil = getFTPUtil();
//        File file = new File("/Users/zhaochengshui/Desktop/4GB.txt");
//        try {
//            ftpUtil.connect();
//            FileInputStream inputStream = new FileInputStream(file);
//            // 监控上传任务，如果期间被强制终止，结束任务
//            new Thread(() -> {
//                try {
//                    monitorFtpUpload(ftpUtil, null, null, inputStream);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }).start();
//            ftpUtil.uploadFile("1.txt", inputStream, "/haha/");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            log.error("", e);
//        } finally {
//            ftpUtil.disconnect();
//        }
//    }
//
//    /**
//     * 方式二：使用java.text.DecimalFormat实现
//     *
//     * @param x
//     * @param y
//     * @return
//     */
//    public static String getPercent2(int x, long y) {
//        double d1 = x * 1.0;
//        double d2 = y * 1.0;
//        // 设置保留几位小数， “.”后面几个零就保留几位小数，这里设置保留四位小数
//        DecimalFormat decimalFormat = new DecimalFormat("##.0000%");
//        return decimalFormat.format(d1 / d2);
//    }
//
//    private void monitorFtpUpload(FTPUtil ftpUtil, String platform, String streamingNo, FileInputStream inputStream) throws InterruptedException {
//        log.info("Upload Task monitor start, Platform: {}, StreamingNo: {}", platform, streamingNo);
//
//        while (ftpUtil.isConnected()) {
//            TimeUnit.SECONDS.sleep(10);
//            if (true) {
//                try {
//                    inputStream.close();
//                } catch (IOException e) {
//                    log.error("", e);
//                }
//                log.info("Upload Task Stop FtpClient, Platform: {}, StreamingNo: {}", platform, streamingNo);
//                ftpUtil.disconnect();
//            }
//            TimeUnit.SECONDS.sleep(10);
//            log.info("Upload Task monitor continue, Platform: {}, StreamingNo: {}", platform, streamingNo);
//
//        }
//        log.info("Upload Task monitor end, Platform: {}, StreamingNo: {}", platform, streamingNo);
//    }
//
//    @Test
//    void threadTest() throws InterruptedException {
//        FTPClientPool ftpClientPool = new FTPClientPool();
////        FTPClient ftpClient = ftpClientPool.borrowObject("localhost:21:username:password");
//        for (int i = 0; i < 20; i++) {
//            int finalI = i;
//            new Thread(
//                    () -> {
//                        FTPClient ftpClient = null;
//                        try {
//                            ftpClient = ftpClientPool.borrowObject("192.168.31.201:21:ftpuser:123456");
//                            FTPUtil ftpUtil = FTPUtil.warpFtpClient(ftpClient);
//                            log.info("start");
//                            ftpUtil.upload("/mmm/ccc/" + finalI + ".data", new File("/Users/zhaochengshui/Desktop/big_file.data"));
//                            log.info("end");
////                            log.info("size:{}", strings.size());
////                            ftpUtil.setFtpClient(null);
//                            // 使用FTPClient对象进行操作
//                            ftpClientPool.returnObject("192.168.31.201:21:ftpuser:123456", ftpClient);
//                        } catch (Exception e) {
//                            log.error("e", e);
//                        }
//
//                    }
//            ).start();
////            TimeUnit.NANOSECONDS.sleep(100);
//
//        }
//
//        TimeUnit.SECONDS.sleep(10000000);
//
//    }
//
//
//    @Test
//    public void tes11t() throws InterruptedException {
//        int aa = 0;
//        for (int i = 0; i < 10000000; i++) {
//            int finalAa = aa;
//            new Thread(
//                    () -> {
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("111", String.valueOf(finalAa));
//                        log.info("map:{}", map);
//                    }
//            ).start();
//            aa++;
//            TimeUnit.NANOSECONDS.sleep(100);
//        }
//
//        TimeUnit.SECONDS.sleep(10000000);
//    }
//
//}
