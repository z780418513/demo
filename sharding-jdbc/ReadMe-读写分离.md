# 读写分离
## 1.环境搭建
使用docker安装3台mysql服务器，组成一主二从
### 1.1 创建mysql的data,log,conf目录（省略）
### 1.2 创建数据库的my.cnf
master的my.cnf
```properties
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=101
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql
## 开启二进制日志功能
log-bin=mysql-bin
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062
```
slave1的my.cnf
```properties
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=102
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能，以备Slave作为其它数据库实例的Master时使用
log-bin=mysql-slave1-bin  
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062  
## relay_log配置中继日志
relay_log=mysql-relay-bin  
## log_slave_updates表示slave将复制事件写进自己的二进制日志
log_slave_updates=1  
## slave设置为只读（具有super权限的用户除外）
read_only=1
```
slave2的my.cnf
```properties
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=103
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能，以备Slave作为其它数据库实例的Master时使用
log-bin=mysql-slave1-bin  
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062  
## relay_log配置中继日志
relay_log=mysql-relay-bin  
## log_slave_updates表示slave将复制事件写进自己的二进制日志
log_slave_updates=1  
## slave设置为只读（具有super权限的用户除外）
read_only=1
```
### 1.3 启动docker

```shell
docker run -p 3303:3306 --name mysql-master \
-v /Users/zhaochengshui/data/mysql/mysql-master/log:/var/log/mysql \
-v /Users/zhaochengshui/data/mysql/mysql-master/data:/var/lib/mysql  \
-v /Users/zhaochengshui/data/mysql/mysql-master/conf:/etc/mysql/conf.d  \
-e MYSQL_ROOT_PASSWORD=root  \
-d mysql:latest
```
```shell
docker run -p 3304:3306 --name mysql-slave1 \
-v /Users/zhaochengshui/data/mysql/mysql-slave1/log:/var/log/mysql \
-v /Users/zhaochengshui/data/mysql/mysql-slave1/data:/var/lib/mysql  \
-v /Users/zhaochengshui/data/mysql/mysql-slave1/conf:/etc/mysql/conf.d  \
-e MYSQL_ROOT_PASSWORD=root  \
-d mysql:latest
```
```shell
docker run -p 3303:3306 --name mysql-slave2 \
-v /Users/zhaochengshui/data/mysql/mysql-slave2/log:/var/log/mysql \
-v /Users/zhaochengshui/data/mysql/mysql-slave2/data:/var/lib/mysql  \
-v /Users/zhaochengshui/data/mysql/mysql-slave2/conf:/etc/mysql/conf.d  \
-e MYSQL_ROOT_PASSWORD=root  \
-d mysql:latest
```
### 1.4 配置主从配置
配置master的主从配置
```shell
# 进入主容器
docker exec -it mysql-master /bin/bash
# 进入mysql 用户名root/root
mysql -uroot -proot
# 创建slave节点用来同步的账户repl/123456
CREATE USER 'repl'@'%' identified with mysql_native_password BY '123456';
# 授权
GRANT REPLICATION SLAVE ON *.* TO 'repl'@'%';
#刷新
FLUSH PRIVILEGES;
# 查看master的信息 记录file 和  position的值 
show master status;
```
例如如下的mysql-bin.000003 ,7938
```text
+-----------------------+----------+--------------+------------------+-------------------+
| File                  | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
+-----------------------+----------+--------------+------------------+-------------------+
| mysql-bin.000003 |     7938 |              | mysql            |                   |
+-----------------------+----------+--------------+------------------+-------------------+
1 row in set (0.00 sec)
```
配置slave1的主从配置
```shell
# 进入主容器
docker exec -it mysql-slave1 /bin/bash
# 进入mysql 用户名root/root
mysql -uroot -proot
# 配置slave节点信息
# master_host: master地址
# master_user: master的账号
# master_password： master的密码
# master_port： master端口
# master_log_file： 监听的master bin_log日志文件
# master_log_pos： 日志的偏移量
change master to master_host='192.168.31.197', master_user='repl', master_password='123456', master_port=3303, master_log_file='mysql-bin.000003', master_log_pos=7938, master_connect_retry=30;
# 开启slave
start slave;
# 查看连接状态
show slave status \G;
```
启动后注意 Slave_IO_Running: Yes ，Slave_SQL_Running: Yes 为yes就表示成功了
```text
*************************** 1. row ***************************
               Slave_IO_State: Waiting for source to send event
                  Master_Host: 192.168.101.85
                  Master_User: repl
                  Master_Port: 3303
                Connect_Retry: 30
              Master_Log_File: mall-mysql-bin.000003
          Read_Master_Log_Pos: 1704
               Relay_Log_File: mysql-relay2-bin.000002
                Relay_Log_Pos: 331
        Relay_Master_Log_File: mall-mysql-bin.000003
             Slave_IO_Running: Yes
            Slave_SQL_Running: Yes
              Replicate_Do_DB: 
```
slave2同理操作
## 2.yml配置
```text
spring:
  sharding-sphere:
    datasource:
      # 配置真实数据源
      names: master,slave1,slave2
      # 数据源1
      master:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3303/goods?allowPublicKeyRetrieval=true
        username: root
        password: root
      # 数据源2
      slave1:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3304/goods?allowPublicKeyRetrieval=true
        username: root
        password: root
      # 数据源3
      slave2:
        type: com.zaxxer.hikari.HikariDataSource
        driver-class-name: com.mysql.cj.jdbc.Driver
        jdbc-url: jdbc:mysql://localhost:3305/goods?allowPublicKeyRetrieval=true
        username: root
        password: root
    # 规则
    rules:
      # 读写分离
      - readwrite-splitting:
          data-sources:
            # 自定义读写规则名
            my_readwrite_ds:
              # 类型
              type: Static
              # 属性
              props:
                # 指定写的数据源 master
                write-data-source-name: master
                # 指定读的数据源 slave1,slave2
                read-data-source-names: slave1,slave2
              # 负载均衡算法名称 (自定义) alg_round
              load-balancer-name: alg_round
          load-balancers:
            # 自定义 alg_round 负载均衡算法配置
            alg_round:
              type: ROUND_ROBIN

    props:
      #是否在日志中打印 SQL。
      sql-show: true
      #是否在日志中打印简单风格的 SQL。
      sql-simple: true

```
## 3.测试
查询测试
```java
    @Test
    void readWriteQueryTest() {
        for (int i = 1; i <= 5; i++) {
            Good good = goodMapper.selectById(i);
            log.info("good[{}]: {}", i, good);
        }

    }
```
```log
2023-04-16 11:32:37.482  INFO 15381 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT id,`name`,price FROM good WHERE id=? 
2023-04-16 11:32:37.482  INFO 15381 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2023-04-16 11:32:37.482  INFO 15381 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [slave1] ::: 1
2023-04-16 11:32:37.518  INFO 15381 --- [           main] c.h.s.ShardingJdbcApplicationTests       : good[1]: Good(id=1, name=毛衣, price=1.00)
2023-04-16 11:32:37.518  INFO 15381 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT id,`name`,price FROM good WHERE id=? 
2023-04-16 11:32:37.518  INFO 15381 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2023-04-16 11:32:37.518  INFO 15381 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [slave2] ::: 1
2023-04-16 11:32:37.525  INFO 15381 --- [           main] c.h.s.ShardingJdbcApplicationTests       : good[2]: Good(id=2, name=null, price=null)
2023-04-16 11:32:37.525  INFO 15381 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT id,`name`,price FROM good WHERE id=? 
2023-04-16 11:32:37.525  INFO 15381 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2023-04-16 11:32:37.525  INFO 15381 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [slave1] ::: 1
2023-04-16 11:32:37.527  INFO 15381 --- [           main] c.h.s.ShardingJdbcApplicationTests       : good[3]: Good(id=3, name=品牌毛巾, price=3.00)
2023-04-16 11:32:37.527  INFO 15381 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT id,`name`,price FROM good WHERE id=? 
2023-04-16 11:32:37.527  INFO 15381 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2023-04-16 11:32:37.527  INFO 15381 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [slave2] ::: 1
2023-04-16 11:32:37.529  INFO 15381 --- [           main] c.h.s.ShardingJdbcApplicationTests       : good[4]: Good(id=4, name=品牌毛巾, price=3.00)
2023-04-16 11:32:37.530  INFO 15381 --- [           main] ShardingSphere-SQL                       : Logic SQL: SELECT id,`name`,price FROM good WHERE id=? 
2023-04-16 11:32:37.530  INFO 15381 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(table=Optional.empty, limit=Optional.empty, lock=Optional.empty, window=Optional.empty)
2023-04-16 11:32:37.530  INFO 15381 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [slave1] ::: 1
2023-04-16 11:32:37.531  INFO 15381 --- [           main] c.h.s.ShardingJdbcApplicationTests       : good[5]: null
```
可以看出来，查询的时候只读取slave1和slave2

新增测试
```log
2023-04-16 11:35:06.286  INFO 15439 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO good  ( id,`name`,price )  VALUES  ( ?,?,? )
2023-04-16 11:35:06.286  INFO 15439 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2023-04-16 11:35:06.286  INFO 15439 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [master] ::: 1
2023-04-16 11:35:06.318  INFO 15439 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO good  ( id,`name`,price )  VALUES  ( ?,?,? )
2023-04-16 11:35:06.318  INFO 15439 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2023-04-16 11:35:06.318  INFO 15439 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [master] ::: 1
2023-04-16 11:35:06.328  INFO 15439 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO good  ( id,`name`,price )  VALUES  ( ?,?,? )
2023-04-16 11:35:06.328  INFO 15439 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2023-04-16 11:35:06.328  INFO 15439 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [master] ::: 1
2023-04-16 11:35:06.333  INFO 15439 --- [           main] ShardingSphere-SQL                       : Logic SQL: INSERT INTO good  ( id,`name`,price )  VALUES  ( ?,?,? )
2023-04-16 11:35:06.334  INFO 15439 --- [           main] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2023-04-16 11:35:06.334  INFO 15439 --- [           main] ShardingSphere-SQL                       : Actual SQL(simple): [master] ::: 1
```
写的时候操作的都是master节点
