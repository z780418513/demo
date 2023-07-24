package com.hb.ftpdemo;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.io.IOException;


public class FTPClientFactory extends BaseKeyedPooledObjectFactory<String, FTPClient> {

    @Override
    public FTPClient create(String key) throws Exception {
        String[] parts = key.split(":");
        String host = parts[0];
        int port = Integer.parseInt(parts[1]);
        String username = parts[2];
        String password = parts[3];
        FTPClient ftpClient = new FTPClient();
        // 连接
        ftpClient.connect(host, port);
        // 登陆
        ftpClient.login(username, password);
        System.out.println("create ftpclient");
        return ftpClient;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return new DefaultPooledObject<>(ftpClient);
    }

    @Override
    public void destroyObject(String key, PooledObject<FTPClient> pooledObject) throws Exception {
        FTPClient ftpClient = pooledObject.getObject();
        ftpClient.logout();
        ftpClient.disconnect();
    }

    @Override
    public boolean validateObject(String key, PooledObject<FTPClient> p) {
        FTPClient ftpClient = p.getObject();
        if (!ftpClient.isConnected()) {
            return false;
        }
        try {
            return ftpClient.sendNoOp();
        } catch (IOException ignored) {
        }
        return false;
    }


}
