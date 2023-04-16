package com.hb.shardingjdbc;

import com.hb.shardingjdbc.mapper.GoodMapper;
import com.hb.shardingjdbc.model.Good;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class ReadWriteTest {
    @Resource
    private GoodMapper goodMapper;


    @Test
    void readWriteQueryTest() {
        for (int i = 1; i <= 5; i++) {
            Good good = goodMapper.selectById(i);
            log.info("good[{}]: {}", i, good);
        }

    }

    @Test
    void readWriteInsert() {
        for (int i = 7; i <= 10; i++) {
            goodMapper.insert(new Good(i, "品牌毛巾" + i, "3.00"));
        }
    }



}
