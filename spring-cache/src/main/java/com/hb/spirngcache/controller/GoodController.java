package com.hb.spirngcache.controller;

import com.hb.spirngcache.model.Good;
import com.hb.spirngcache.service.GoodService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/19
 */
@RestController
public class GoodController {
    @Resource
    private GoodService goodService;

    @GetMapping("/getById")
    public String getById(@RequestParam("id") Integer id) {
        Good good = goodService.getById(id);
        return good.toString();
    }

    @DeleteMapping("/del/{id}")
    public String del(@PathVariable("id") Integer id) {
        goodService.delById(id);
        return "ok";
    }

    @PutMapping("/upd")
    public String updateGood(@RequestBody Good good) {
        goodService.updateById(good);
        return "ok";
    }

    @PostMapping("/add")
    public String addGood(@RequestBody Good good) {
        goodService.add(good);
        return "ok";
    }
}
