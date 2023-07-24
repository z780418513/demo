package com.hb.test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.URLUtil;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import lombok.Data;
import org.junit.Test;

import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/5/23
 */
public class URLUtilTest {
    @Test
    public void buildUrl() {
//        URL url = URLUtil.url("mds/special/image/20230509/juzhao2_600_338_140559.jpg");
        URL url = URLUtil.url("ftp://ftpuser:123456@192.168.5.7/mds/special/image/20230509/");
//        URL url = URLUtil.url("https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=%E4%B8%80%E4%B8%AAftp%E5%9C%B0%E5%9D%80%E4%BC%9A%E6%9C%89%E4%B8%8D%E5%90%8C%E7%9A%84%E5%9C%B0%E5%9D%80%E5%90%97&oq=%25E4%25B8%2580%25E4%25B8%25AAftp%25E5%258F%25AF%25E4%25BB%25A5%25E6%259C%2589%25E5%25A4%259A%25E4%25B8%25AA%25E6%2596%2587%25E4%25BB%25B6%25E5%25A4%25B9%25E5%2590%2597&rsv_pq=928ba7410002ebec&rsv_t=969cPLIk8tnRr0gp0Rh7wgUvE8Xh82KloLpwGSfOm%2F3VwcozNOmIhXvVVnU&rqlang=cn&rsv_dl=tb&rsv_enter=0&rsv_btype=t&inputT=17967&rsv_sug3=108&rsv_sug1=40&rsv_sug7=100&sug=%25E4%25B8%2580%25E4%25B8%25AAftp%25E5%259C%25B0%25E5%259D%2580%25E4%25BC%259A%25E6%259C%2589%25E4%25B8%258D%25E5%2590%258C%25E7%259A%2584%25E5%259C%25B0%25E5%259D%2580%25E5%2590%2597&rsv_n=1&bs=%E4%B8%80%E4%B8%AAftp%E5%8F%AF%E4%BB%A5%E6%9C%89%E5%A4%9A%E4%B8%AA%E6%96%87%E4%BB%B6%E5%A4%B9%E5%90%97");
//        URL url = URLUtil.url("ftp://icds02:icds02@10.251.7.37:21/RHSC_ceshi/u-wihr2s79aci31445/zhuanma/transfer_done/20230505/98d253b502704ad480e9e019c8bee414.ts");
//        URL url = URLUtil.url("ftp://icds02:icds02@10.251.7.37:21");
        String ftpClientKey = "ftp://" + url.getAuthority();
//        url = URLUtil.url(ftpClientKey);


        System.out.println("url.saa() = " + url.getProtocol() + "://" + url.getAuthority());
        System.out.println("url.getAuthority() = " + url.getAuthority());
        System.out.println("url.getProtocol() = " + url.getProtocol());
        System.out.println("url.getHost() = " + url.getHost());
        System.out.println("url.getUserInfo() = " + url.getUserInfo());
        System.out.println("url.getFile() = " + url.getFile());

        // icds02:icds02@10.251.7.37:21
        FtpPlatformConfigDto ftpConfig = new FtpPlatformConfigDto();
        String userInfoStr = url.getUserInfo();
        String[] userInfo = userInfoStr.split(":");
        ftpConfig.setUsername(userInfo[0]);
        ftpConfig.setPassword(userInfo[1]);
        ftpConfig.setHost(url.getHost());
        ftpConfig.setPort(String.valueOf(url.getPort()));

        System.out.println("ftpConfig = " + ftpConfig);

    }
}

@Data
class FtpPlatformConfigDto {
    private String host;
    private String port;
    private String username;
    private String password;
    private boolean useConfigClient = false;
}
