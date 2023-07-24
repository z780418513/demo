package com.hb.shardingjdbc;

import java.util.Set;

/**
 * 分表策略
 *
 * @author zhaochengshui
 * @description
 * @date 2023/6/27
 */
public interface ShardingStrategy {

    /**
     * 获取实际分表表名
     *
     * @param logicTableName 逻辑表名
     * @return 实际分表表名
     */
    Set<String> getResultTableNames(String logicTableName);
}
