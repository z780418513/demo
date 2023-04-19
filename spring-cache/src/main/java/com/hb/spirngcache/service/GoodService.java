package com.hb.spirngcache.service;

import com.hb.spirngcache.model.Good;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/19
 */
public interface GoodService {

    Good getById(Integer id);

    void updateById(Good good);

    void delById(Integer id);

    Good add(Good good);
}
