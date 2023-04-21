package com.hb.stream.rocketmq;

import com.hb.stream.rocketmq.mq.MySink;
import com.hb.stream.rocketmq.mq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

//rocketmq
@EnableBinding({ MySource.class, MySink.class })
@SpringBootApplication
public class StreamRocketmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamRocketmqApplication.class, args);
    }

}
