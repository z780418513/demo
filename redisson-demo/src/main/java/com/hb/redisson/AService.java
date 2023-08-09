package com.hb.redisson;

import org.springframework.stereotype.Service;

@Service
public class AService {

    @RedisLock(lockKey = "output_key:", lockKeyId = "#id")
    public void doaaa(String id) {
        System.out.println("aaaaaa");
    }
}
