package com.hb.springxml.service;

import com.hb.springxml.model.TestRequest;
import com.hb.springxml.model.TestResponse;
import com.hb.springxml.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    public String service(TestRequest request) {
        TestResponse response = new TestResponse();
        response.setCode("200");
        response.setMsg("allow sub");
        response.setActiveFlag("1");
        return XmlUtil.javaBeanToXml(response);
    }

}
