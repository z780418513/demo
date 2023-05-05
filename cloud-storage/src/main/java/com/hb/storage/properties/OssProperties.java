package com.hb.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhaochengshui
 * @description
 * @date 2022/9/22
 */
@ConfigurationProperties(prefix = "aliyun")
public class OssProperties {

    /**
     * 开启oss
     */
    private boolean enableOss;

    /**
     * Endpoint
     */
    private String endpoint;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    public boolean isEnableOss() {
        return enableOss;
    }

    public void setEnableOss(boolean enableOss) {
        this.enableOss = enableOss;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
