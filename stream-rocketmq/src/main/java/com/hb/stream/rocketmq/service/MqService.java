package com.hb.stream.rocketmq.service;

import org.springframework.messaging.Message;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
public interface MqService {
    /**
     * 发送rocketmq消息
     *
     * @param channelName 消息发送者名
     * @param tag         tag
     * @param message     消息
     * @return true=成功
     * @author zhaochenghui
     */
    boolean sendMessage(String channelName, String tag, String message);

}
