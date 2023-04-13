package com.hb.prometheus.service.impl;

import com.hb.prometheus.monitor.PrometheusMeterMonitor;
import com.hb.prometheus.monitor.PrometheusMetricEnum;
import com.hb.prometheus.service.PrometheusMetricService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/13
 */
@Service
public class PrometheusMetricServiceImpl implements PrometheusMetricService {

    @Resource
    private PrometheusMeterMonitor prometheusMeterMonitor;

    @Override
    public void initMeter(PrometheusMetricEnum metricEnum, Long initNum, String... keyParams) {
        String meterKey = String.format(metricEnum.getKey(), keyParams);
        if (metricEnum.getMeter() == Counter.class) {
            prometheusMeterMonitor.initCounterByKey(meterKey, Double.valueOf(initNum));
        } else if (metricEnum.getMeter() == Gauge.class) {
            prometheusMeterMonitor.initGaugeByKey(meterKey, initNum);
        }
    }

    @Override
    public void incrMeter(PrometheusMetricEnum metricEnum, Long initNum, String... keyParams) {
        String meterKey = String.format(metricEnum.getKey(), keyParams);
        if (metricEnum.getMeter() == Counter.class) {
            prometheusMeterMonitor.incrCounterByKey(meterKey, Double.valueOf(initNum));
        } else if (metricEnum.getMeter() == Gauge.class) {
            prometheusMeterMonitor.incrGaugeByKey(meterKey, initNum);
        }
    }
}
