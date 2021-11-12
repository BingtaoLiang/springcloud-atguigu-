#1. 分布式事务问题
    分布式之前，单机单库没有这个问题，在分布式系统中，可能会有订单微服务，物流微服务，金融微服务等不同的服务，对应着订单数据库，物流数据库，金融数据库，
    在一整个流程中涉及到多个服务，多个数据库之间的联合操作，逻辑上这些多个的数据库操作是一个整体。 
    原来的单体应用被拆分成微服务应用，原来的三个模块被拆分成三个独立的应用，分别使用三个独立的数据源，业务操作需要调用三个服务来完成，
    此时每个服务内部的数据一致性由本地事务来保证，但是全局的数据一致性问题没办法保证。
   一句话：一次业务操作需要跨多个数据源或需要跨多个系统进行远程调用，就会产生分布式事务问题
   
#2. Seata简介
 ##2.1 是什么
    Seata 是一款开源的分布式事务解决方案，致力于在微服务架构下提供高性能和简单易用的分布式事务服务。
    官网：http://seata.io/zh-cn/
 ##2.2 能干嘛
    一个典型的分布式事务过程
       分布式事务处理过程的一ID+3组件模型
       一ID：Transaction ID--XID 全局唯一的事务ID
       三组件：
            Transaction Coordinator(TC)--事务协调者:维护全局和分支事务的状态，驱动全局事务提交或回滚。
            Transaction Manager(TM)--事务管理器:定义全局事务的范围：开始全局事务、提交或回滚全局事务。
            Resource Manager(RM)--资源管理器:管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚。
    
    处理过程：
       1.TM向TC申请开启一个全局事务，全局事务创建成功并生成一个全局唯一的XID;
       2.XID在微服务调用链路的上下文中传播;
       3.RM向TC注册分支事务，将其纳入XID对应全局事务的管辖;
       4.TM向TC发起针对 XID的全局提交或回滚决议;
       5.TC调度XID下管辖的全部分支事务完成提交或回滚请求。

 ##2.3 去哪下
    https://github.com/seata/seata/releases/download/v1.4.2/seata-server-1.4.2.zip
 
 ##2.4 怎么玩
    本地 @Transactional
    全局 @GlobalTransactional  Seat的分布式交易解决方案，我们只需要使用一个@GlobalTransactional注解在业务方法上
    

#3. Seata-Server安装
    1. Seata-server-1.4.2.zip解压到指定目录并修改conf目录下的file.conf配置文件
       - 先备份原始file.conf文件
       - 主要修改：自定义事务组名称+事务日志存储模式为db+数据库连接信息
       - file.conf改service模块+store模块(10.4.2版本没有service)
    2. mysql数据库新建库seata
    3. 在seata库里建表
       具体语句如下:
       ```
       -- -------------------------------- The script used when storeMode is 'db' --------------------------------
       -- the table to store GlobalSession data
       CREATE TABLE IF NOT EXISTS `global_table`
       (
           `xid`                       VARCHAR(128) NOT NULL,
           `transaction_id`            BIGINT,
           `status`                    TINYINT      NOT NULL,
           `application_id`            VARCHAR(32),
           `transaction_service_group` VARCHAR(32),
           `transaction_name`          VARCHAR(128),
           `timeout`                   INT,
           `begin_time`                BIGINT,
           `application_data`          VARCHAR(2000),
           `gmt_create`                DATETIME,
           `gmt_modified`              DATETIME,
           PRIMARY KEY (`xid`),
           KEY `idx_gmt_modified_status` (`gmt_modified`, `status`),
           KEY `idx_transaction_id` (`transaction_id`)
       ) ENGINE = InnoDB
         DEFAULT CHARSET = utf8;
       
       -- the table to store BranchSession data
       CREATE TABLE IF NOT EXISTS `branch_table`
       (
           `branch_id`         BIGINT       NOT NULL,
           `xid`               VARCHAR(128) NOT NULL,
           `transaction_id`    BIGINT,
           `resource_group_id` VARCHAR(32),
           `resource_id`       VARCHAR(256),
           `branch_type`       VARCHAR(8),
           `status`            TINYINT,
           `client_id`         VARCHAR(64),
           `application_data`  VARCHAR(2000),
           `gmt_create`        DATETIME(6),
           `gmt_modified`      DATETIME(6),
           PRIMARY KEY (`branch_id`),
           KEY `idx_xid` (`xid`)
       ) ENGINE = InnoDB
         DEFAULT CHARSET = utf8;
       
       -- the table to store lock data
       CREATE TABLE IF NOT EXISTS `lock_table`
       (
           `row_key`        VARCHAR(128) NOT NULL,
           `xid`            VARCHAR(128),
           `transaction_id` BIGINT,
           `branch_id`      BIGINT       NOT NULL,
           `resource_id`    VARCHAR(256),
           `table_name`     VARCHAR(32),
           `pk`             VARCHAR(36),
           `gmt_create`     DATETIME,
           `gmt_modified`   DATETIME,
           PRIMARY KEY (`row_key`),
           KEY `idx_branch_id` (`branch_id`)
       ) ENGINE = InnoDB
         DEFAULT CHARSET = utf8;
       
       CREATE TABLE IF NOT EXISTS `distributed_lock`
       (
           `lock_key`       CHAR(20) NOT NULL,
           `lock_value`     VARCHAR(20) NOT NULL,
           `expire`         BIGINT,
           primary key (`lock_key`)
       ) ENGINE = InnoDB
         DEFAULT CHARSET = utf8mb4;
       
       INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('AsyncCommitting', ' ', 0);
       INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryCommitting', ' ', 0);
       INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('RetryRollbacking', ' ', 0);
       INSERT INTO `distributed_lock` (lock_key, lock_value, expire) VALUES ('TxTimeoutCheck', ' ', 0);
       ```
    4. 修改 seata-server-1.4.2\seata\conf目录下的registry.conf配置文件
    5. 先启动Nacos端口号8848
    6. 在启动seatea-server
    
    
    
#4. 订单/库存/账户业务数据库准备
    1. 一下演示都需要先启动Nacos再启动Seata ，保证两个都ok
    2. 分布式事务业务说明
       这里我们会创建三个服务，一个订单服务，一个库存服务，一个账户服务。
       当用户下单时，会在订单服务中创建一个订单，然后通过远程调用库存服务来扣减下单商品的库存，再通过远程调用账户服务来扣减用户账户里面的余额，
       最后在订单服务中修改订单状态为已完成。|
       该操作跨越三个数据库，有两次远程调用，很明显会有分布式事务问题。
       下订单-->扣库存-->减余额
    3. 创建业务数据库
       seata_order:存储订单的数据库
       seate_storage: 存储库存的数据库
       seata_account: 存储账户信息的数据库
       
       CREATE DATABASE seata_order;
       CREATE DATABASE seate_storage;
       CREATE DATABASE seata_account;
    4. 按照上述3个库分别建立对应业务表
       seata_order库下建t_order表
            CREATE TABLE t_order(
            `id` BIGINT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `user_id` BIGINT(11) DEFAULT NULL COMMENT'用户id',
            `product_id` BIGINT(11) DEFAULT NULL COMMENT '产品id',
            `count`INT(11) DEFAULT NULL COMMENT '数量',
            `money` DECIMAL(11,0) DEFAULT NULL COMMENT'金额',
            `status`INT(1) DEFAULT NULL COMMENT'订单状态:0:创建中;1:已完结'
            )ENGINE=INNODB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
            SELECT *FROM t_order;

       seate_storage库下建t_storage表
            CREATE TABLE t_storage (
            `id` BlGINT(11)NOT NULL AUTO_INCREMENT PRIMARY KEY,
            `product_id` BIGINT(11) DEFAULT NULL COMMENT '产品id',
            `total` INT(11) DEFAULT NULL COMMENT'总库存',
            `used` INT(11) DEFAULT NULL COMMENT '已用库存',
            `residue` INT(11) DEFAULT NULL COMMENT'剩余库存'
            )ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

       seata_account库下建t_account表
            CREATE TABLE t_account (
            `id` BIGINT(11)NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
            `user_id` BIGINT(11) DEFAULT NULL COMMENT'用户id',
            `total` DECIMAL(10,0)DEFAULT NULL COMMENT'总额度',
            `used` DECIMAL(10,0)DEFAULT NULL COMMENT '已用余额',
            `residue` DECIMAL(10,0)DEFAULT 0 COMMENT'剩余可用额度
            )ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
            INSERT INTO seata_account.t_account(`id` ,`user_id` ,`total` , `used` ,`residue`)
              VALUES ('1', '1','1000','0','1000');
            SELECT * FROM t_account;

    5. 按照上述3个库分别建立对应回滚日志表
       订单-库存-账户3个库下都需要建各自的回滚日志表，下边的建表代码在三个业务库中都执行一遍
       -- the table to store seata xid data
       -- 0.7.0+ add context
       -- you must to init this sql for you business databese. the seata server not need it.
       -- 此脚本必须初始化在你当前的业务数据库中，用于AT 模式XID记录。与server端无关（注：业务数据库）
       -- 注意此处0.3.0+ 增加唯一索引 ux_undo_log
       -- drop table `undo_log`;
       CREATE TABLE `undo_log` (
         `id` bigint(20) NOT NULL AUTO_INCREMENT,
         `branch_id` bigint(20) NOT NULL,
         `xid` varchar(100) NOT NULL,
         `context` varchar(128) NOT NULL,
         `rollback_info` longblob NOT NULL,
         `log_status` int(11) NOT NULL,
         `log_created` datetime NOT NULL,
         `log_modified` datetime NOT NULL,
         `ext` varchar(100) DEFAULT NULL,
         PRIMARY KEY (`id`),
         UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
       ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
    6. 最终效果
      最终数据库目录如下
       seata:
          - seata_order
          - seata_storage
          - seata_account
       seata_account:
          - t_account
          - undo_log
       seata_order:
          - t_order
          - undo_log
       seata_storage:
          - t_storage
          - undo_log
          
          
#5. 订单/库存/账户业务微服务准备
    1. 业务需求
       下订单-->减库存-->扣余额-->改订单(状态)
    2. 新建订单Order-Module
       - seata-order-service2001
       - pom
       - yml
       - file.conf
       - registry.conf
       - domain
          CommonResult.java
          Order.java
       - Dao接口及实现
          Order.java
       - service接口及实现
       - controller
       - config配置
          1. MyBatisConfig
          2. DataSourceProxyConfig
       - 主启动
    3. 新建库存Storage-Module
       同订单模块
    4. 新建账户Account-Module
       同订单模块
    
    5. 测试  下订单-->减库存-->扣余额-->改订单(状态)
      - 正常下单 测试成功
      - 超时异常 
           没加@GlobalTransactional  AccountServiceImpl超时
           当库存和账户金额扣减后，订单状态并没有设置为已完成，没有从0改为1
           而且由于feign的重试机制，账户余额还有可能被多次扣减
      - 超时异常，添加@GlobalTransactional     