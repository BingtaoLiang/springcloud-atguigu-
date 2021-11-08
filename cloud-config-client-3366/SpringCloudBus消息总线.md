# 1. 概述
   - 是对Config配置中心的扩充，分布式自动刷新配置功能，Spring Cloud Bus配合Spring Cloud Config 使用可以实现配置的动态刷新
   - Bus支持两种消息代理：RabbitMQ和Kafka
   - Spring Cloud Bus 是用来将分布式系统的节点与轻量级消息系统连接起来的框架，它整合了java的时间处理机制和消息中间件功能，目前支持RabbitMQ和Kafka
   
 ## 1.1 什么是总线
   在微服务架构的系统中，通常会使用轻量级的消息代理来构建一个共用的消息主题，并让系统中所有微服务实例都连接上来。由于该主题中产生的消息会被所有实例监听和消费，所以称它为消息总线。在总线上的各个实例，都可以方便地广播一些需要让其他连接在该主题上的实例都知道的消息。
 ## 1.2 基本原理
   ConfigClient实例都监听MQ中同一个topic(默认是springCloudBus)。当一个服务刷新数据的时候，它会把这个信息放入到Topic中，这样其它监听同一Topic的服务就能得到通知，然后去更新自身的配置。
   
   
# 2. RabbitMQ环境配置
  2.1 安装Erlang
  2.2 安装RabbitMQ
  2.3 进入RabbitMQ安装目录下的sbin目录
  2.4 输入以下命令启动管理功能
      rabbitmq-plugins enable rabbitmq_management
  2.5 访问地址查看是否安装成功 -- http://localhost:15672/
  2.6 输入账号密码并登录：guest,guest 

# 3. SpringCloud Bus 动态刷新全局广播
 - 必须具备良好的RabbitMQ环境先
 - 演示广播效果，增加复杂度，再以3355为模板制作一个3366模块
 - 设计思想
   1.  利用消息总线触发一个客户端/bus/refresh，而刷新所有客户端的配置
   2. **利用消息总线触发一个服务端ConfigServer的/bus/refresh端点，而刷新所有客户端的配置**
   第二种架构更加合适，第一种不合适的原因如下：
   1. 打破了微服务的职责单一性，因为微服务本身是业务模块，它本身不应该承担配置刷新的职责
   2. 破坏了微服务各节点的对等性
   3. 有一定的局限性。例如，微服务在迁移时，他的网络地址常常会发生变化，此时如果想要做到自动刷新，那就会增加更多的修改
 
 ## 3.1 步骤
  1. 给3344配置中心服务端添加消息总线
   pom:
   ```html
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-bus-amqp</artifactId>
      </dependency>
   ```
   yml:
   ```yaml
      #rabbitmq相关配置
      rabbitmq:
        host: localhost
        port: 5672
        username: guest
        password: guest
      
      ##rabbitmq相关配置,暴露bus刷新配置的端点
      management:
        endpoints: #暴露bus刷新配置的端点
          web:
            exposure:
              include: 'bus-refresh'
  ```
  2. 3355和3366做相同的修改，pom文件中添加amqp依赖，yml中添加rabbitmq的连接
  3. 测试
     - 运维工程师修改github上配置文件增加版本号
     - 发送post请求  curl -X POST "http://localhost:3344/actuator/bus-reresh" 刷的是配置中心3344，而不是每台客户端
     - 配置中心3344，客户端3355和3366获取配置信息，发现都已经刷新了
  4. 一次修改，广播通知，处处生效

# 4. SpringCloud Bus动态刷新定点通知(精确打击)
  - 不想全部通知，指向定点通知(只通知3355,不通知3366)
  - 简单一句话：
    ```text
        指定具体某一个实例生效而不是全部
        公式：http://localhost:配置中心端口号(3344)/actuator/bus-refresh/{destination}
        /bus/refresh请求不在发送到具体的服务实例上，而是发给config server并通过destination参数类指定需要更新配置的服务或实例
    ```
  - 案例
    ```text
        我们这里以刷新运行在3355端口上的config-client为例(只通知3355,不通知3366)
        curl -X POST "http://localhost:3344/actuator/bus-refresh/config-client:3355"
    ```
  最终实现3355客户端变更,3366客户端不变更的效果。
  