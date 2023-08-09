package com.hb.redisson;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author SYM
 * @date 2022/6/6 9:09
 * @Description EpEL工具类
 */
public class SpelUtils {

    /**
     * 支持 #p0 参数索引的表达式解析
     * @param rootObject 根对象,method 所在的对象
     * @param spel 表达式
     * @param method ，目标方法
     * @param args 方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject,String spel, Method method, Object[] args) {
        if (StringUtils.isEmpty(StringUtils.trim(spel))) {
            return StringUtils.EMPTY;
        }
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        if (CollectionUtils.isEmpty(Arrays.asList(paraNameArr))) {
            return spel;
        }
        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject,method,args,u);
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}
