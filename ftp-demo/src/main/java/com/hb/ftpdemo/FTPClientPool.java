package com.hb.ftpdemo;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FTPClientPool {
    private GenericKeyedObjectPool<String, FTPClient> ftpClientPool;

    public FTPClientPool() {
        GenericKeyedObjectPoolConfig<FTPClient> config = new GenericKeyedObjectPoolConfig<>();
        config.setMaxTotalPerKey(10); // 每个key最大连接数
        config.setMinIdlePerKey(0); // 每个key最小空闲连接数
        config.setMaxIdlePerKey(10); // 每个key最大空闲连接数
        config.setTestOnBorrow(true); // 借用对象时是否进行有效性检查
        config.setTestOnReturn(true); // 归还对象时是否进行有效性检查
        config.setTestOnCreate(true); // 创建时是否进行有效性检查
        config.setTestWhileIdle(true); // 是否在空闲时检查对象的有效性
        ftpClientPool = new GenericKeyedObjectPool<>(new FTPClientFactory(), config);
    }

    public FTPClient borrowObject(String key) throws Exception {
        FTPClient ftpClient = ftpClientPool.borrowObject(key);
        configureFTPClient(ftpClient);
        return ftpClient;
    }

    public void returnObject(String key, FTPClient ftpClient) {
        resetFTPClient(ftpClient);
        ftpClientPool.returnObject(key, ftpClient);
    }

    private void configureFTPClient(FTPClient ftpClient) throws IOException {
        ftpClient.setBufferSize(1024 * 1024);
        ftpClient.setDataTimeout(60000);       //设置传输超时时间为60秒
        ftpClient.setConnectTimeout(60000);       //连接超时为60秒
        //设置控制端口的心跳超时时间，便于自动发送控制端口心跳检测包
        ftpClient.setControlKeepAliveTimeout(6);
        ftpClient.setControlKeepAliveReplyTimeout(60000);
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.enterLocalPassiveMode();
    }

    private void resetFTPClient(FTPClient ftpClient) {
        try {
            ftpClient.noop();
        } catch (IOException e) {
            // ignore
        }
        try {
            ftpClient.changeWorkingDirectory("/");
        } catch (IOException e) {
            // ignore
        }
    }
}

