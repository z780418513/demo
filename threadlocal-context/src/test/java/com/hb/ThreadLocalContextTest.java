package com.hb;

import com.hb.thread.ThreadLocalContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/21
 */
public class ThreadLocalContextTest {

    @Test
    public void test(){
        ThreadLocalContext.setHolderByKey("k1",111111);
        ThreadLocalContext.setHolderByKey("k2","ffdfd");
        Map<String, Object> map = ThreadLocalContext.getHolderMapByKeys("k1", "k2","k3");
        System.out.println("map = " + map);
    }

    @Test
    public void test2(){
        ThreadLocalContext.setHolderByKey("k1",new ArrayList<>());
        List<Object> k1 = ThreadLocalContext.getHolderListByKey("k1");
        System.out.println("k1 = " + k1);
    }
}
