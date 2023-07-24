package com.hb.shardingjdbc;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.RuleConfiguration;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.springframework.core.env.Environment;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分片算法工具
 * @author zhaochengshui
 */
@Slf4j
public class ShardingAlgorithmTool {

    /**
     * 表分片符号，例：t_contract_202201 中，分片符号为 "_"
     */
    private static final String TABLE_SPLIT_SYMBOL = "_";

    /**
     * 数据库配置
     */
    private static final Environment ENV = SpringUtil.getApplicationContext().getEnvironment();
    private static final String DATASOURCE_URL = ENV.getProperty("spring.shardingsphere.datasource.database1.url");
    private static final String DATASOURCE_USERNAME = ENV.getProperty("spring.shardingsphere.datasource.database1.username");
    private static final String DATASOURCE_PASSWORD = ENV.getProperty("spring.shardingsphere.datasource.database1.password");
    private static final String DB_NAME = ENV.getProperty("spring.shardingsphere.datasource.names");


    /**
     * 重载全部缓存
     */
    public static void tableNameCacheReloadAll() {
        Arrays.stream(ShardingTableCacheEnum.values()).forEach(ShardingAlgorithmTool::tableNameCacheReload);
    }

    /**
     * 重载指定分表缓存
     *
     * @param logicTable 逻辑表，例：t_contract
     */
    public static void tableNameCacheReload(ShardingTableCacheEnum logicTable) {
        // 读取数据库中所有表名
        List<String> tableNameList = getAllTableNameBySchema(logicTable);
        // 删除旧的缓存（如果存在）
        logicTable.getResultTableNamesCache().clear();
        // 写入新的缓存
        logicTable.getResultTableNamesCache().addAll(tableNameList);
        // 动态更新配置 actualDataNodes
        actualDataNodesRefresh(logicTable);
    }

    /**
     * 获取所有表名
     *
     * @param logicTable 逻辑表
     * @return 表名集合
     */
    public static List<String> getAllTableNameBySchema(ShardingTableCacheEnum logicTable) {
        List<String> tableNames = new ArrayList<>();
        if (StringUtils.isBlank(DATASOURCE_URL) || StringUtils.isBlank(DATASOURCE_USERNAME) || StringUtils.isBlank(DATASOURCE_PASSWORD)) {
            log.error("DB config error，please check the account ，URL:{}, username:{}, password:{}", DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD);
            throw new IllegalArgumentException("数据库连接配置有误，请稍后重试");
        }
        try (Connection conn = DriverManager.getConnection(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD);
             Statement st = conn.createStatement()) {
            String logicTableName = logicTable.getLogicTableName();
            try (ResultSet rs = st.executeQuery("show TABLES like '" + logicTableName + TABLE_SPLIT_SYMBOL + "%'")) {
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    tableNames.add(tableName);
                }
            }
        } catch (SQLException e) {
            log.error("Connection to db error, Message: {}", e.getMessage(), e);
            throw new IllegalArgumentException("数据库连接失败，请稍后重试");
        }
        return tableNames;
    }

    /**
     * 动态更新配置 actualDataNodes
     *
     * @param logicTable
     */
    public static void actualDataNodesRefresh(ShardingTableCacheEnum logicTable) {
        try {
            String logicTableName = logicTable.getLogicTableName();
            Set<String> tableNamesCache = logicTable.getResultTableNamesCache();
            log.info("Dynamic to update actualDataNodes，logicTableName:{}，tableNamesCache:{}", logicTableName, tableNamesCache);

            // generate actualDataNodes
            String newActualDataNodes = tableNamesCache.stream().map(o -> String.format("%s.%s", DB_NAME, o)).collect(Collectors.joining(","));
            ShardingSphereDataSource shardingSphereDataSource = SpringUtil.getBean(ShardingSphereDataSource.class);

            // 重新加载新配置
            updateShardRuleActualDataNodes(shardingSphereDataSource, logicTableName, newActualDataNodes);
        } catch (Exception e) {
            log.error("初始化 动态表单失败，原因：{}", e.getMessage(), e);
        }
    }


    // --------------------------------------------------------------------------------------------------------------
    // 私有方法
    // --------------------------------------------------------------------------------------------------------------


    /**
     * 刷新ActualDataNodes
     */
    private static void updateShardRuleActualDataNodes(ShardingSphereDataSource dataSource, String logicTableName, String newActualDataNodes) {
        // Context manager.
        ContextManager contextManager = dataSource.getContextManager();

        // Rule configuration.
        String schemaName = "logic_db";
        Collection<RuleConfiguration> newRuleConfigList = new LinkedList<>();
        Collection<RuleConfiguration> oldRuleConfigList = dataSource.getContextManager()
                .getMetaDataContexts()
                .getMetaData(schemaName)
                .getRuleMetaData()
                .getConfigurations();

        for (RuleConfiguration oldRuleConfig : oldRuleConfigList) {
            if (oldRuleConfig instanceof AlgorithmProvidedShardingRuleConfiguration) {
                AlgorithmProvidedShardingRuleConfiguration oldAlgorithmConfig = (AlgorithmProvidedShardingRuleConfiguration) oldRuleConfig;
                Collection<ShardingTableRuleConfiguration> oldTableRuleConfigList = oldAlgorithmConfig.getTables();

                Collection<ShardingTableRuleConfiguration> newTableRuleConfigList = new LinkedList<>();
                oldTableRuleConfigList.forEach(oldTableRuleConfig -> {
                    if (logicTableName.equals(oldTableRuleConfig.getLogicTable())) {
                        ShardingTableRuleConfiguration newTableRuleConfig = new ShardingTableRuleConfiguration(logicTableName, newActualDataNodes);
                        newTableRuleConfig.setTableShardingStrategy(oldTableRuleConfig.getTableShardingStrategy());
                        newTableRuleConfig.setDatabaseShardingStrategy(oldTableRuleConfig.getDatabaseShardingStrategy());
                        newTableRuleConfig.setKeyGenerateStrategy(oldTableRuleConfig.getKeyGenerateStrategy());

                        newTableRuleConfigList.add(newTableRuleConfig);
                    } else {
                        newTableRuleConfigList.add(oldTableRuleConfig);
                    }
                });

                AlgorithmProvidedShardingRuleConfiguration newAlgorithmConfig = new AlgorithmProvidedShardingRuleConfiguration();
                newAlgorithmConfig.setTables(newTableRuleConfigList);
                newAlgorithmConfig.setAutoTables(oldAlgorithmConfig.getAutoTables());
                newAlgorithmConfig.setBindingTableGroups(oldAlgorithmConfig.getBindingTableGroups());
                newAlgorithmConfig.setBroadcastTables(oldAlgorithmConfig.getBroadcastTables());
                newAlgorithmConfig.setDefaultDatabaseShardingStrategy(oldAlgorithmConfig.getDefaultDatabaseShardingStrategy());
                newAlgorithmConfig.setDefaultTableShardingStrategy(oldAlgorithmConfig.getDefaultTableShardingStrategy());
                newAlgorithmConfig.setDefaultKeyGenerateStrategy(oldAlgorithmConfig.getDefaultKeyGenerateStrategy());
                newAlgorithmConfig.setDefaultShardingColumn(oldAlgorithmConfig.getDefaultShardingColumn());
                newAlgorithmConfig.setShardingAlgorithms(oldAlgorithmConfig.getShardingAlgorithms());
                newAlgorithmConfig.setKeyGenerators(oldAlgorithmConfig.getKeyGenerators());

                newRuleConfigList.add(newAlgorithmConfig);
            }
        }

        // update context
        contextManager.alterRuleConfiguration(schemaName, newRuleConfigList);
    }


    public static void createShardingTableAndReload(ShardingTableCacheEnum logicTable, String channel) {
        Class<? extends ShardingStrategy> shardingStrategy = logicTable.getShardingStrategy();
        if (shardingStrategy == ChannelHashModShardingStrategy.class) {
            ChannelHashModShardingStrategy strategy = new ChannelHashModShardingStrategy();
            strategy.setChannel(channel);
            String sql = "CREATE TABLE IF NOT EXISTS `%s` LIKE `%s`;";

            List<String> sqlList =
                    strategy.getResultTableNames(logicTable.getLogicTableName())
                            .stream()
                            .map(resultTableName -> String.format(sql, resultTableName, logicTable.getLogicTableName()))
                            .collect(Collectors.toList());

            // 执行建表sql
            executeSql(sqlList);
            // 缓存重载
            tableNameCacheReload(logicTable);
        }
    }

    /**
     * 执行SQL
     *
     * @param sqlList SQL集合
     */
    private static void executeSql(List<String> sqlList) {
        if (StringUtils.isBlank(DATASOURCE_URL) || StringUtils.isBlank(DATASOURCE_USERNAME) || StringUtils.isBlank(DATASOURCE_PASSWORD)) {
            log.error("DB config error，please check the account ，URL:{}, username:{}, password:{}", DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD);
            throw new IllegalArgumentException("数据库连接配置有误，请稍后重试");
        }
        try (Connection conn = DriverManager.getConnection(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD)) {
            try (Statement st = conn.createStatement()) {
                conn.setAutoCommit(false);
                for (String sql : sqlList) {
                    st.execute(sql);
                }
            } catch (SQLException e) {
                conn.rollback();
                log.error("Create Db SQL FAIL, e:{}", e.getMessage(), e);
                throw new IllegalArgumentException("数据表创建执行失败，请稍后重试");
            }
        } catch (SQLException e) {
            log.error("Execute SQL FAIL, e:{}", e.getMessage(), e);
            throw new IllegalArgumentException("数据库连接失败，请稍后重试");
        }
    }

}
