package com.hb;

import com.hb.thread.context.MdcIdContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/20
 */
@Slf4j
public class MdcTest {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    public void test() {
        // 单线程
        try {
            for (int i = 0; i < 10; i++) {
                log.info("单线程执行 TrackId:{}", MdcIdContext.getTrackId());
            }
        } catch (Exception e) {
            log.error("error", e);
        } finally {
            MdcIdContext.remove();
        }

        // 多线程
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    log.info("多线程执行 TrackId:{}", MdcIdContext.getTrackId());
                } catch (Exception e) {
                    log.error("error", e);
                } finally {
                    MdcIdContext.remove();
                }
            });
        }


    }
}
