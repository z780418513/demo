package com.hb.spirngcache.service.impl;

import com.hb.spirngcache.mapper.GoodMapper;
import com.hb.spirngcache.model.Good;
import com.hb.spirngcache.service.GoodService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/19
 */
@Service
public class GoodServiceImpl implements GoodService {
    @Resource
    private GoodMapper goodMapper;
    private static final String GOOD_CACHE = "springcache.goodCache";


    @Override
    @Cacheable(value = GOOD_CACHE, key = "#id", unless = "#result == null")
    public Good getById(Integer id) {
        return goodMapper.selectById(id);
    }

    @Override
    @CacheEvict(value = GOOD_CACHE, key = "#good.id")
    public void updateById(Good good) {
        goodMapper.updateById(good);
    }

    @Override
    @CacheEvict(value = GOOD_CACHE, key = "#id")
    public void delById(Integer id) {
        goodMapper.deleteById(id);
    }

    @Override
    @CachePut(value = GOOD_CACHE, key = "#good.id")
    public Good add(Good good) {
        goodMapper.insert(good);
        return good;
    }
}
