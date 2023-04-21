package com.hb.stream.rocketmq.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 生成者source
 * <p>  @Output 中的值需与配置文件中的保持一致
 * <p>例如:@Output("demo01-output"） ==>配置文件中的 spring.cloud.stream.bindings.demo01-output.destination
 *
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
public interface MySource {

    @Output(SourceSinkConst.DEMO01_OUTPUT)
    MessageChannel demo01Output();

}
