package com.hb.prometheus.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Tag;
import lombok.Getter;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/12
 */
@Getter
public enum PrometheusMetricEnum {
    TASK_TOTAL("%s_task_total", Counter.class, "", null),
    TASK_UNCOMPLETED_TOTAL("%s_task_uncompleted_total", Gauge.class, "", null),
    TASK_PROGRESSING_TOTAL("%s_task_progressing_total", Gauge.class, "", null),
    ;

    /**
     * meter key
     */
    private final String key;
    /**
     * meter class
     */
    private final Class<? extends Meter> meter;
    /**
     * Metric 对应的redisKey
     */
    private final String redisKey;
    /**
     * tags (Counter类型需要)
     */
    private final Iterable<Tag> tags;

    PrometheusMetricEnum(String key, Class<? extends Meter> meter, String redisKey, Iterable<Tag> tags) {
        this.key = key;
        this.meter = meter;
        this.redisKey = redisKey;
        this.tags = tags;
    }

}
