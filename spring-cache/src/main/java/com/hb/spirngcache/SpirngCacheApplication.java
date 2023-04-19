package com.hb.spirngcache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching // 开启缓存
@MapperScan("com.hb.spirngcache.mapper")
public class SpirngCacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpirngCacheApplication.class, args);
    }

}
