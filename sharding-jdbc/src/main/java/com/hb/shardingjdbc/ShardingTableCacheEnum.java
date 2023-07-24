package com.hb.shardingjdbc;

import lombok.Getter;

import java.util.*;

/**
 * <p> @Title ShardingTableCacheEnum
 * <p> @Description 分片表缓存枚举
 *
 * @author ACGkaka
 * @date 2022/12/23 20:17
 */
@Getter
public enum ShardingTableCacheEnum {

    /**
     * 用户表
     */
    STUDENT("student", new HashSet<>(), ChannelHashModShardingStrategy.class);


    /**
     * 逻辑表名
     */
    private final String logicTableName;
    /**
     * 实际表名
     */
    private final Set<String> resultTableNamesCache;
    /**
     * 分表策略
     */
    private final Class<? extends ShardingStrategy> shardingStrategy;


    ShardingTableCacheEnum(String logicTableName, Set<String> resultTableNamesCache, Class<? extends ShardingStrategy> shardingStrategy) {
        this.logicTableName = logicTableName;
        this.resultTableNamesCache = resultTableNamesCache;
        this.shardingStrategy = shardingStrategy;
    }


}
