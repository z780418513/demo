package com.hb.stream.rocketmq.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
@Service
public class RocketMQService implements MqService {

    /**
     * 通过spring注入 获得一个bean的name为key，bean的对象为value的map
     */
    @Resource
    public Map<String, MessageChannel> map = new HashMap<>();

    @Override
    public boolean sendMessage(String channelName, String tag, String message) {
        MessageChannel messageChannel = getMessageChannelByName(channelName);
        if (Objects.isNull(messageChannel)) {
            throw new RuntimeException(String.format("Has not messageChannel bean , name is :%s", channelName));
        }
        return sendMessage(messageChannel, tag, message);
    }


    private boolean sendMessage(MessageChannel channel, String tag, String message) {
        Message<String> msg = MessageBuilder
                .withPayload(message)
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
                .build();
        return channel.send(msg);
    }

    /**
     * 获取MessageChannel对象
     *
     * @param name MessageChannel的bean对象名
     * @return MessageChannel
     */
    private MessageChannel getMessageChannelByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        if (!map.containsKey(name)) {
            return null;
        }
        return map.get(name);
    }
}
