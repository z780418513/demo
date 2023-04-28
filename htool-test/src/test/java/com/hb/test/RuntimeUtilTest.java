package com.hb.test;

import cn.hutool.core.util.RuntimeUtil;
import org.junit.Test;

import java.util.List;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/28
 */
public class RuntimeUtilTest {
    @Test
    public void exec(){
        // 本地执行docker 命令
        List<String> dockerImages = RuntimeUtil.execForLines("docker images");
        dockerImages.forEach(System.out::println);
    }
}
