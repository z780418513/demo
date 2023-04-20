package com.hb;

import com.hb.entiy.Good;
import com.hb.entiy.Order;
import com.hb.thread.ThreadLocalContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/20
 */
@Slf4j
public class PayContextTest {

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    public void test() {

        Good good1 = Good.builder().id(1L).name("毛衣大衣").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
        String goodKey1 = String.format("good:%s", 1);
        Good good2 = Good.builder().id(2L).name("中山装").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
        String goodKey2 = String.format("good:%s", 2L);
        String orderKey = String.format("order:%s", 3L);
        Order order = Order.builder().id(3L).createTime(new Date()).build();

        try {
            ThreadLocalContext.setHolderByKey(goodKey1, good1);
            ThreadLocalContext.setHolderByKey(goodKey2, good2);
            ThreadLocalContext.setHolderByKey(orderKey, order);
            for (int i = 0; i < 3; i++) {
                foreachGet(goodKey1, goodKey2, orderKey);
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ThreadLocalContext.reseat();
        }

    }


    @Test
    public void test2() {
        try {
            for (int i = 0; i < 3; i++) {
                executorService.execute(() -> {
                    Good good1 = Good.builder().id(1L).name("毛衣大衣").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
                    String goodKey1 = String.format("good:%s", 1);
                    Good good2 = Good.builder().id(2L).name("中山装").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
                    String goodKey2 = String.format("good:%s", 2L);
                    String orderKey = String.format("order:%s", 3L);
                    Order order = Order.builder().id(3L).createTime(new Date()).build();
                    ThreadLocalContext.setHolderByKey(goodKey1, good1);
                    ThreadLocalContext.setHolderByKey(goodKey2, good2);
                    ThreadLocalContext.setHolderByKey(orderKey, order);
                    foreachGet(goodKey1, goodKey2, orderKey);
                });

            }
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ThreadLocalContext.reseat();
        }

    }


    public void foreachGet(String goodKey1, String goodKey2, String orderKey) {
        Good good1 = ThreadLocalContext.getLocalByKey(goodKey1);
        Good good2 = ThreadLocalContext.getLocalByKey(goodKey2);
        Order order = ThreadLocalContext.getLocalByKey(orderKey);
        log.info("good1 :{}", good1);
        log.info("good2 :{}", good2);
        log.info("order :{}", order);

    }


    @Test
    public void test3() {
        try {
            for (int i = 0; i < 3; i++) {
                executorService.execute(() -> {
                    Good good1 = Good.builder().id(1L).name("毛衣大衣").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
                    Good good2 = Good.builder().id(2L).name("中山装").price(new BigDecimal(ThreadLocalRandom.current().nextInt(10))).build();
                    ArrayList<Object> list = new ArrayList<>();
                    list.add(good1);
                    list.add(good2);
                    ThreadLocalContext.setHolderByKey("goodList", list);
                    doLog("goodList");
                });

            }
            TimeUnit.SECONDS.sleep(3);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ThreadLocalContext.reseat();
        }
    }

    private void doLog(String key) {
        List<Good> goods = ThreadLocalContext.getHolderListByKey(key);
        log.info("{}", goods);
    }
}
