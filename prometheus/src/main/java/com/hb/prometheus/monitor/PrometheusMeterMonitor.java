package com.hb.prometheus.monitor;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 平台任务监控
 *
 * @author zhaochengshui
 * @description
 * @date 2023/4/11
 */
@Component
public class PrometheusMeterMonitor {
    /**
     * 存储 counter 指标
     * <p> key : 指标名
     * <p> value : 对应的Counter
     */
    public final ConcurrentMap<String, Counter> counterMap = new ConcurrentHashMap<>();
    /**
     * 存储 gauge 指标
     * <p> key : 指标名
     * <p> value : 对应的gauge值
     */
    public final ConcurrentMap<String, AtomicLong> gaugeMap = new ConcurrentHashMap<>();

    @Resource
    private MeterRegistry meterRegistry;


    /**
     * 初始化Counter
     *
     * @param counterKey CounterKey
     * @param initNum    初始值
     */
    public void initCounterByKey(String counterKey, Double initNum) {
        initCounterByKey(counterKey, initNum, null);
    }

    /**
     * 初始化Counter
     *
     * @param counterKey CounterKey
     * @param initNum    初始值
     * @param tags       tags
     */
    public void initCounterByKey(String counterKey, Double initNum, Iterable<Tag> tags) {
        if (Objects.isNull(getCounterByKey(counterKey))) {
            Optional.ofNullable(registryCounter(counterKey, tags))
                    .ifPresent((registryCounter) -> registryCounter.increment(initNum));
        }
    }

    /**
     * 初始化Counter
     *
     * @param gaugeKey gaugeKey
     * @param initNum  初始值
     */
    public void initGaugeByKey(String gaugeKey, Long initNum) {
        if (Objects.isNull(getGaugeByKey(gaugeKey))) {
            registryGauge(gaugeKey, new AtomicLong(initNum));
        }
    }

    /**
     * Counter递增
     *
     * @param counterKey counterKey
     * @param num        incr值
     */
    public void incrCounterByKey(String counterKey, double num) {
        Counter counter = counterMap.get(counterKey);
        if (Objects.isNull(counter)) {
            initCounterByKey(counterKey, num);
        } else {
            counter.increment(num);
        }
    }

    /**
     * Gauge递增
     *
     * @param gaugeKey counterKey
     * @param num      incr值
     */
    public void incrGaugeByKey(String gaugeKey, long num) {
        AtomicLong atomicDouble = gaugeMap.get(gaugeKey);
        if (Objects.isNull(atomicDouble)) {
            initGaugeByKey(gaugeKey, num);
        } else {
            atomicDouble.addAndGet(num);
        }
    }


    private Counter registryCounter(String key, Iterable<Tag> tags) {
        Counter counter = null;
        if (Objects.isNull(counterMap.get(key))) {
            if (Objects.isNull(tags)) {
                counter = meterRegistry.counter(key);
            } else {
                counter = meterRegistry.counter(key, tags);
            }
            counterMap.put(key, counter);
        }
        return counter;
    }

    private AtomicLong registryGauge(String key, AtomicLong atomicDouble) {
        AtomicLong gauge = null;
        if (Objects.isNull(gaugeMap.get(key))) {
            gauge = meterRegistry.gauge(key, atomicDouble);
            gaugeMap.put(key, gauge);
        }
        return gauge;
    }

    public Counter getCounterByKey(String key) {
        return counterMap.get(key);
    }

    public AtomicLong getGaugeByKey(String key) {
        return gaugeMap.get(key);
    }


}
