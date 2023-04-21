package com.hb.stream.rocketmq.consumer;

import com.hb.stream.rocketmq.mq.SourceSinkConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
@Service
@Slf4j
public class Demo01Consumer {

    /**
     * 监听 DEMO01_INPUT 的消费者
     *
     * @param message
     */
    @StreamListener(SourceSinkConst.DEMO01_INPUT)
    public void receiveMessage(Message<String> message) {
        log.info(message.getPayload());
    }
}

