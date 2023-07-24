package com.hb.shardingjdbc;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 渠道+关键字hash取模 分表策略
 *
 * @author zhaochengshui
 * @description
 * @date 2023/6/27
 */
@Data
@Getter
@Setter
public class ChannelHashModShardingStrategy implements ShardingStrategy {
    /**
     * 渠道
     */
    private String channel;
    /**
     * 分片数量（默认8）
     */
    private int shardingCount = 8;

    @Override
    public Set<String> getResultTableNames(String logicTableName) {
        HashSet<String> resultTableNames = new HashSet<>();
        for (int i = 0; i < shardingCount; i++) {
            String resultTableName = logicTableName + "_" + channel + "_" + i;
            resultTableNames.add(resultTableName);
        }
        return resultTableNames;
    }
}
