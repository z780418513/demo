package com.hb.test;

import cn.hutool.core.lang.Console;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RuntimeUtil;
import org.junit.Test;

import java.math.RoundingMode;
import java.util.List;

/**
 * @author zhaochengshui
 * @description
 * @date 2023/4/28
 */
public class NumberUtilTest {
    @Test
    public void math() {
        double a = -1.30;
        double b = 5.36;
        // 加减乘除
        Console.log(NumberUtil.add(a, b));
        Console.log(NumberUtil.sub(a, b));
        Console.log(NumberUtil.sub(a, 55));
        Console.log(NumberUtil.div(b, 3));
        // 保留2位小数 具体策略 RoundingMode
        Console.log(NumberUtil.round(NumberUtil.div(b, 3), 2, RoundingMode.HALF_UP));
    }


    @Test
    public void math2() {
        double a = -1.30;
        double b = 5.36;
        // 加减乘除
        Console.log(NumberUtil.add(a, b));
        Console.log(NumberUtil.sub(a, b));
        Console.log(NumberUtil.sub(a, 55));
        Console.log(NumberUtil.div(b, 3));
        Console.log(NumberUtil.div(b, 3,2));
        // 保留2位小数 具体策略 RoundingMode
        Console.log(NumberUtil.round(NumberUtil.div(b, 3), 2, RoundingMode.HALF_UP));
    }
}
