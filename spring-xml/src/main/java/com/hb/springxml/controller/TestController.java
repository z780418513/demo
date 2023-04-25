package com.hb.springxml.controller;

import com.hb.springxml.model.TestRequest;
import com.hb.springxml.service.TestService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    private TestService testService;

    // xml 报文请求
    @PostMapping(value = "/test", produces = MediaType.APPLICATION_XML_VALUE)
    // 响应是string 格式 xml
    public String test(@RequestBody TestRequest request) {
        return testService.service(request);
    }
}
