package com.hb.storage.config;

import com.hb.storage.properties.OssProperties;
import com.hb.storage.service.AliyunOssService;
import com.hb.storage.service.impl.AliyunOssServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author zhaochengshui
 * @description
 * @date 2022/9/22
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class OssConfig {
    @Resource
    private OssProperties ossProperties;

    @ConditionalOnProperty(prefix = "aliyun", name = "enable-oss", havingValue = "true")
    @Bean(name = "aliyunOssService")
    public AliyunOssService ossService() {
        return new AliyunOssServiceImpl(ossProperties);
    }
}
