package com.hb.shardingjdbc;

import com.hb.shardingjdbc.mapper.TOrderMapper;
import com.hb.shardingjdbc.mapper.TUserMapper;
import com.hb.shardingjdbc.model.TOrder;
import com.hb.shardingjdbc.model.TUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/16
 */
@SpringBootTest
@Slf4j
public class BTest {
    @Resource
    private TUserMapper tUserMapper;
    @Resource
    private TOrderMapper tOrderMapper;

    @Test
    void queryTest2() {
        TUser tUser1 = tUserMapper.selectById(1);
        System.out.println("tUser = " + tUser1);
        TUser tUser2 = tUserMapper.selectById(2);
        System.out.println("tUser2 = " + tUser2);

        TOrder tOrder = tOrderMapper.selectById(2);
        System.out.println("tOrder = " + tOrder);
    }

    @Test
    void insert2() {
        tUserMapper.insert(new TUser(2, "小王", "xuiewuiew"));
        tOrderMapper.insert(new TOrder(2, "dsds", new BigDecimal("2.00"), "1"));
    }
}
