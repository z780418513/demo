package com.hb.redisson;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AService {

    @RedisLock(lockName = "#id")
    public void doaaa(String id) {
        System.out.println("aaaaaa");
    }
}
