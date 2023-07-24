package com.hb.redisson;

import org.springframework.stereotype.Service;

@Service
public class AService {

    @RedisLock
    public void doaaa(){
        System.out.println("aaaaaa");
    }
}
