# 1. 概述
 ## 1.1 是什么
  - 是什么：屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型
  - 官方定义Spring Cloud Stream 是一个构建消息驱动微服务的框架，应用程序通过inputs或者outputs来与Spring cloud stream中binder对象交互。
    通过我们配置来binding(绑定)，而Spring Cloud Stream的binder对象负责与消息中间件交互。所以，我们只需要搞清楚如何与Spring cloud stream 交互就可以方便使用消息驱动的方式
    
    通过使用spring Integration来连接消息代理中间件以实现消息事件驱动。
    Spring Cloud Stream为一些应用上的消息中间件产品提供了个性化的自动化配置实现，引用了发布-订阅、消费组、分区的三个核心概念
  - 目前仅支持RabbitMQ、Kafka
  
 ## 1.2 设计思想
  标准MQ：
  1. 生产者/消费者之间考消息媒介传递消息内容-Message
  2. 消息必须走特定的通道-消息通道MessageChannel
  3. 消息通道里的消息如何被消费呢？谁负责收发处理-消息通道MessageChannel的子接口SubscribableChannel，由MessageHandler消息处理器所订阅
  这些中间件的差异性导致我们实际项目开发给我们造成了一定的困扰，我们如果用了两个消息队列的其中一种，后面的业务需求，我想往另外一种消息队列进行迁移，
  这时候无疑就是一个灾难性的，一大堆东西都要重新推倒重新做，因为它跟我们的系统耦合了，这时候springcloud Stream给我们提供了—种解耦合的方式。

  **Binder**
  input对应于生产者
  output对应于消费者
  通过定义绑定器Binder作为中间层，实现了应用程序与消息中间件细节之间的隔离。
  stream中的消息通信方式遵循了发布-订阅模式，使用topic主题进行广播，在RabbitMQ中就是exchange，在kafka中就是Topic
 
  **spring cloud stream标准流程套路**
  消息生产者-->业务逻辑-->SpringCloud Stream[Source-->Channel-->Binder]-->MQ组件(RabbitMQ/Kafka)<--Stream[Binder<--Channel<--Sink]<--业务逻辑<--消息消费者 
  - Binder:很方便的连接中间件，屏蔽差异
  - Channel:消息管道
  - Source和Sink:简单的恶意理解为参照对象时SpringCloud Stream自身，从Stream发布消息就是输出，接收消息就是输入
 
  **编码API和常用注解**
  - Middleware ：中间件，目前只支持RabbitMQ和Kafka
  - Binder ： Binder是应用与消息中间件之间的封装，目前实行了Kafka和RabbitMQ的Binder，通过Binder可以很方便的连接中间件，可以动态的改变消息类型(对应于Kafka的topic,RabbitMQ的exchange)，这些都可以通过配置文件来实现
  - @Input ： 注解标识输入通道，通过该输入通道接收到的消息进入应用程序
  - @Output ： 注解标识输出通道，发布的消息将通过该通道离开应用程序
  -StreamListener ： 监听队列，用于消费者的队列的消息接收
  - EnableBinding： 指信道channel和exchange绑定在一起


# 2. 案例说明
 ## 2.1 新建三个子模块
    cloud-stream-rabbitmq-provider8801,作为生产者进行发消息模块
      测试，启动7001eureka，启动rabbitmq,启动8801,访问localhost:8801/sendMessage即可看到控制台输出，并且在localhot:15672能看到曲线变化
    cloud-stream-rabbitmq-consumer8802,作为消息接收模块
      再启动8802模块用于接收消息。
    cloud-stream-rabbitmq-consumer8803，作为消息接收模块
    
 ## 2.2 分组消费与持久化
   - 依照8802，clone出来一份运行8803，cloud-stream-rabbitmq-consumer8803，作为消息接收模块
   - 启动rabbitMQ,7001,8801,8802,8803
   - 启动之后有两个问题
     ### 2.2.1. 有重复消费问题
        8801发送消息，8802和8803都能同时接收到消息，重复消费。
        导致原因：默认分组是不同的，组流水号不一样，被认为不同组，可以重复消费。
     ```text
        比如在如下场景中，订单系统我们做集群部署，都会从RabbitMQ中获取订单信息，那如果一个订单同时被两个服务获取到，
        那么就会造成数据错误，我们得避免这种情况。这时我们就可以使用Stream中的消息分组来解决.
        
        在Stream中处于同一个group中的多个消费者是竞争关系，就能保证消息只会被其中一个应用消费一次，不同组是可以全面消费的(重复消费)
     ```
     - 解决原理：微服务应用放置于同一个group中，就能保证消息只会被其中一个应用消费一次，不同的组是可以消费的，同一个组内会发生竞争关系，只有其中一个可以消费。
     - 自定义配置不同的组，在8802和8803的yml文件中添加group分组信息,此时他们属于不同分组，依然存在重复消费问题
     - 8802/8803实现轮询分组，每次只有一个消费者，8801模块发的消息只能被8802或8803其中一个接收到，将他们分为同一组，这样避免了重复消费，
     - 结论：同一个组的多个微服务实例，每次只会有一个拿到
     ---------------------------------------------------------------------------------------
     ### 2.2.2. 消息持久化问题
        通过上述，解决了重复消费问题，再看看持久化。
        停止8802/8803并去除8802分组的group:group_A,8803的分组没有去除
        8801先发送4条消息到rabbitMQ
        先启动8802，无分组属性配置，后台没有打出来消息
        在启动8803，有分组属性配置，后台打出来了MQ上的消息，可以对未消费的消息进行接收
        
     
   
 