package com.hb.shardingjdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.complex.ComplexKeysShardingValue;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
* @author: LCG
* @date: 2022-11-12 18:52:43
* @description: 自定义 多字段分片算法 按照数据库划分
**/
@Slf4j
public class MyComplexShardingAlgorithmDB implements ComplexKeysShardingAlgorithm<String> {

    private static final String column1="id";

    private static final String column2="class_name";

    /**
     * @author: LCG
     * @date: 2022-11-12 23:19:16
     * @description:  collection  数据库的逻辑名称集合
     *                complexKeysShardingValue  复合分片字段的字段名和字段值
     */
    @Override
    public Collection<String> doSharding(Collection<String> collection, ComplexKeysShardingValue<String> complexKeysShardingValue) {

        String logicTableName = complexKeysShardingValue.getLogicTableName();
        //等于号   in 的值
        Map<String, Collection<String>> columnNameAndShardingValuesMap = complexKeysShardingValue.getColumnNameAndShardingValuesMap();
        //实现  >，>=, <=，<  和 BETWEEN AND 等操作
        Map columnNameAndRangeValuesMap = complexKeysShardingValue.getColumnNameAndRangeValuesMap();
 
        Collection<String> c1 = columnNameAndShardingValuesMap.get(column1);
        Collection<String> c2 = columnNameAndShardingValuesMap.get(column2);
        if(Optional.ofNullable(c1).isPresent()&&Optional.ofNullable(c2).isPresent()){
            String id = c1.stream().findFirst().get();
            String className = c2.stream().findFirst().get();

            int i = id.toString().hashCode() % collection.size();
            String s = i == 0 ? "d0" : "d1";
            return Arrays.asList(s);
        }
        return collection;
    }


    @Override
    public void init() {
    }

    @Override
    public String getType() {
        return "COMPLEX-DB";
    }
}
