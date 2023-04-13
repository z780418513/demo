package com.hb.prometheus.controller;

import com.hb.prometheus.monitor.TaskTotalMeter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/13
 */
@RestController
public class TaskTestController {
    @Resource
    private TaskTotalMeter taskTotalMeter;


    @GetMapping("add") // 简单一点
    public String add(String key) {
        taskTotalMeter.startTask(key);
        return "ok";
    }
}
