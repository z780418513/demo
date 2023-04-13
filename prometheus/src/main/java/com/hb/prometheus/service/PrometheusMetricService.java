package com.hb.prometheus.service;

import com.hb.prometheus.monitor.PrometheusMetricEnum;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/13
 */
public interface PrometheusMetricService {


    void initMeter(PrometheusMetricEnum metricEnum, Long initNum, String... keyParams);

    void incrMeter(PrometheusMetricEnum metricEnum, Long initNum, String... keyParams);
}
