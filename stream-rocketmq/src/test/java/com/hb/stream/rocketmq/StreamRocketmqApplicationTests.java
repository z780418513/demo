package com.hb.stream.rocketmq;

import com.alibaba.fastjson.JSON;
import com.hb.stream.rocketmq.mq.SourceSinkConst;
import com.hb.stream.rocketmq.service.RocketMQService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class StreamRocketmqApplicationTests {
    @Resource
    private RocketMQService rocketMQService;

    @Test
    void sendMessage() {
        for (int i = 0; i < 10; i++) {
            rocketMQService.sendMessage(SourceSinkConst.DEMO01_OUTPUT, "aaa", JSON.toJSONString("发送测试消息" + i));

        }
    }

}
