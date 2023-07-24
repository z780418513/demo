//package com.hb.ftpdemo;
//
//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;
//
//import java.io.IOException;
//import java.net.UnknownHostException;
//
///**
// * @author zhaochengshui
// * @description
// * @date 2023/7/6
// */
//public class FtpTemplate {
//    private FtpProperties ftpProperties;
//
//
//    public FtpTemplate(FtpProperties ftpProperties) {
//        this.ftpProperties = ftpProperties;
//    }
//
//    private FTPClient createFtpClient() throws IOException {
//        FTPClient ftpClient = new FTPClient();
//        ftpClient.connect(ftpProperties.getHost(), ftpProperties.getPort());
//
//        int reply = ftpClient.getReplyCode();
//        if (!FTPReply.isPositiveCompletion(reply)) {
//            disconnect(ftpClient);
//        }
//
//        if (!ftpClient.login(ftpProperties.getUsername(), ftpProperties.getPassword())) {
//            disconnect(ftpClient);
//        }
//
//        // set data transfer mode.
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
//
//        // Use passive mode to pass firewalls.
//        ftpClient.enterLocalPassiveMode();
//        return ftpClient;
//    }
//
//
//    public void upload(String localPath, String remote) {
//
//    }
//
//
//    /**
//     * 关闭ftp连接
//     */
//    public void disconnect(FTPClient ftpClient) {
//        if (null != ftpClient && ftpClient.isConnected()) {
//            try {
//                ftpClient.logout();
//                ftpClient.disconnect();
//            } catch (IOException ex) {
//                // do nothing
//            }
//        }
//    }
//}
