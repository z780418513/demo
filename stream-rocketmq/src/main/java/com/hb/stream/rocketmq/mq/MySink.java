package com.hb.stream.rocketmq.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
public interface MySink {

    @Input(SourceSinkConst.DEMO01_INPUT)
    SubscribableChannel demo01Input();
}
