package com.hb.redisson;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class RedissonDemoApplicationTests {
    @Resource
    private AService aService;

    @Test
    void contextLoads() {
        aService.doaaa("11");
    }

}
