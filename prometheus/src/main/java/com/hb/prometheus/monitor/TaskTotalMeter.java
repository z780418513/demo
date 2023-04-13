package com.hb.prometheus.monitor;

import com.google.common.collect.Lists;
import com.hb.prometheus.service.PrometheusMetricService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/13
 */
@Component
public class TaskTotalMeter {
    @Resource
    private PrometheusMetricService prometheusMetricService;

    @PostConstruct
    public void init() {
        prometheusMetricService.initMeter(PrometheusMetricEnum.TASK_TOTAL, 10L, "all");
        prometheusMetricService.initMeter(PrometheusMetricEnum.TASK_UNCOMPLETED_TOTAL, 10L, "all");
        prometheusMetricService.initMeter(PrometheusMetricEnum.TASK_PROGRESSING_TOTAL, 10L, "all");
    }

    public void startTask(String key) {
        ArrayList<String> platforms = Lists.newArrayList(key, "all");
        for (String platformStr : platforms) {
            prometheusMetricService.incrMeter(PrometheusMetricEnum.TASK_TOTAL, 1L, platformStr);
            prometheusMetricService.incrMeter(PrometheusMetricEnum.TASK_UNCOMPLETED_TOTAL, 1L, platformStr);
            prometheusMetricService.incrMeter(PrometheusMetricEnum.TASK_PROGRESSING_TOTAL, 1L, platformStr);        }
    }

}
