package com.hb.test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.net.url.UrlBuilder;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/28
 */
public class UrlBuilderTest {
    @Test
    public void buildUrl() {
        String url = UrlBuilder
                .of()
                .setScheme("https")
                .setHost("www.baidu.cn")
                .setCharset(StandardCharsets.UTF_8)
                .addPath("/s")
                .addQuery("wd", "测试")
                .build();
        Console.log(url);
    }
}
