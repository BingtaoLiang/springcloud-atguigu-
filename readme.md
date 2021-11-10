## 微服务模块流程
    1. 建module
    2. 改pom
    3. 写yml
    4. 主启动
    5. 业务类

## Eureka、Zookeeper、Consul三者的区别
   Eureka是CP,其余两个是AP.
   CAP:
   C(一致性):所有节点上的数据时刻保持同步
   A(可用性):每个请求都能接收到一个相应，无论相应成功或失败
   P(分区容错性):系统应该能持续提供服务，即使系统内部有消息丢失(分区)

高可用、数据一致是很多系统设计的目标，但是分区又是不可避免的事情：
CA without P：如果不要求P（不允许分区），则C（强一致性）和A（可用性）是可以保证的。但其实分区不是你想不想的问题，而是始终会存在，因此CA的系统更多的是允许分区后各子系统依然保持CA。
CP without A：如果不要求A（可用），相当于每个请求都需要在Server之间强一致，而P（分区）会导致同步时间无限延长，如此CP也是可以保证的。很多传统的数据库分布式事务都属于这种模式。
AP wihtout C：要高可用并允许分区，则需放弃一致性。一旦分区发生，节点之间可能会失去联系，为了高可用，每个节点只能用本地数据提供服务，而这样会导致全局数据的不一致性。现在众多的NoSQL都属于此类。

    
    
## 模块介绍
    1. cloud-api-commons --公共模块，将公共的实体类进行封装打包，供其他模块使用
    2. cloud-consumer-order80 --服务消费者模块，80端口
    3. cloud-consumerconsul-order80 --引入了consul的服务消费者模块
    4. cloud-consumerzk-order80 --引入了Zookeeper的服务消费者模块
    5. cloud-eureka-server7001 --Eureka注册中心7001
    6. cloud-eureka-server7002 --Eureka注册中心7002
    7. cloud-provider-payment8001  --使用Eureka注册中心的服务提供者
    8. cloud-provider-payment8002  --使用Eureka注册中心的服务提供者
    9. cloud-provider-payment8004  --使用Zookeeper注册中心的服务提供者
    10. cloud-providerconsul-payment8006  --使用Consul注册中心的服务提供者
    11. cloud-consumer-feign-order  --使用OpenFeign进行服务调用
    12. cloud-provider-hystrix-payment8001 --服务提供方8001使用hystrix进行服务降级
    13. cloud-consumer-feign-hystrix-order80 --服务调用方使用hystrix进行服务降级（一般服务降级加在客户端较多）
    14. cloud-consumer-hystrix-dashboard9001 --hystrix dashboard 图形化的服务监控
    15. cloud-gateway-gateway9527  --Gateway网关微服务
    16. cloud-config-center3344 -- Config配置服务中心
    17. cloud-config-center3355 -- Config配置服务客户端
    18. cloud-config-center3366 -- Config配置服务客户端
    19. cloud-stream-rabbitmq-provider8801,作为生产者进行发消息模块
    20. cloud-stream-rabbitmq-consumer8802,作为消息接收模块
    21. cloud-stream-rabbitmq-consumer8803，作为消息接收模块
    22. cloudalibaba-provider-payment9001,SpringCloud Alibaba Nacos注册中心服务提供者，Nacos不像Eureka，不用写类似7001的注册中心了，Nacos服务启动后，直接访问8848Nacos页面
    23. cloudalibaba-provider-payment9002,SpringCloud Alibaba Nacos注册中心服务提供者
    24. cloudalibaba-consumer-nacos-order83，SpringCloud Alibaba Nacos注册中心服务消费者
    
