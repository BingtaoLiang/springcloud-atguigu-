# 什么是Hystrix断路器

    在复杂的分布式体系结构中，应用程序可能会有数十个依赖关系，每个依赖关系在某些时候将不可避免的失败。
    比如一个请求需要调用A,P,H,I四个服务，当一切顺利的情况下没什么问题，但是当其中一个服务I超时时，会出现什么情况呢？
  
  **服务雪崩**
  
    多个微服务之间调用的时候，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其它的微服务，这就是所谓的“扇出”。
    如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，
    所谓的“雪崩效应”.
    对于高流量的应用来说，单一的后端依赖可能会导致所有服务器上的所有资源都在几秒钟内饱和。比失败更糟糕的是，
    这些应用程序还可能导致服务之间的延迟增加，备份队列，线程和其他系统资源紧张，导致整个系统发生更多的级联故障。
    这些都表示需要对故障和延迟进行隔离和管理，以便单个依赖关系的失败，不能取消整个应用程序或系统。
    所以，通常当你发现一个模块下的某个实例失败后，这时候这个模块依然还会接收流量，
    然后这个有问题的模块还调用了其他的模块，这样就会发生级联故障，或者叫雪崩。

  **Hystrix**
  
    Hystrix是一个用于处理分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，
    比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，不会导致整体服务失收，避免级联故障，以提高分布式系统的弹性.。
    
    "断路器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝)，
    向调用方返回一个符合预期的、可处理的备选响应(FallBack)，而不是长时间的等待或者抛出调用方无法处理的异常，
    这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。

# Hystrix重要概念
## 服务降级(fallback)
   当服务提供方发生故障时，向调用方返回一个备选响应（fallback）,提供一个兜底的解决方案
   
   `哪些情况会触发降级？`
   1. 程序运行异常
   2. 超时
   3. 服务熔断触发服务降级
   4. 线程池/信号量打满也会导致服务降级
   
## 服务熔断(break)
    类比保险丝达到最大访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法并返回友好提示 
## 服务限流(flowlimit)---在阿里巴巴的sentinel中进行详细介绍
    秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行
    
## 存在的问题 
    当我们使用Jmeter进行压力测试的时候，让20000个请求去访问`paymentInfo_TimeOut`服务，
    这时候，位于同一个微服务内的原本不需要等待的`paymentInfo_OK`服务也需要进行等待，这是因为tomcat的默认工作线程数被打满了，
    没有多余的线程来分解压力和处理。上面还只是服务提供者8001自己测试，假如此时外部的消费者80也来访问，那么消费者只能干等，
    最终导致消费端80不满意，服务端8001直接被拖死。

## 解决方案，解决的要求
    超时导致服务器变慢(转圈)--超时不再等待
    出错(宕机或程序运行出错)--出错要有兜底
    解决：
    1. 对方服务（8001）超时了，调用者（80）不能一直卡死等待，必须有服务降级
    2. 对方服务（8001）down机了，调用者（80）不能一直卡死等待，必须有服务降级
    3. 对方服务（8001）OK，调用者（80）自己出故障或有自我要求(自己的等待时间小于服务提供者)，自己降级处理 
    
## 服务降级
   - 降级配置  @HystrixCommand
   - 8001端先从自身找问题
     ```
     设置自身调用超时时间的阈值，阈值内可以正常运行，超过了需要有兜底的方法处理，做服务降级fallback
     1. 业务类启用@HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
                          @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "3000")
                  })指定兜底方法以及触发兜底方法的阈值，
     2. 主启动类添加@EnableCircuitBreaker   //启用服务降级的注解
     当服务中有两个异常，一个运行异常，一个超时异常，只要当前服务不可用，做服务降级，兜底的方案都是paymentInfo_TimeOutHandler
     ```
   - 80端fallback  
     ```html
     80订单微服务，也可以更好的保护自己，自己也一样进行客户端降级保护  
     Hystrix服务降级既可以放在服务端，也可以放在客户端，一般放在客户端
     **题外话：我们自己配置过的热部署方式对java代码的改动明显，但是对HystrixCommand内属性的修改建议重启微服务**
     1. yml配置
        feign:
          hystrix:
            enabled: true
     2. 主启动类启用支持，加上@EnableHystrix注解
     3. 业务类(controller)中同样要加上注解
        @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod", commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500")
            })和兜底方法
     
     ```    
   - 存在的问题
     1. 兜底的方法与业务方法存放在同一文件中，耦合度过大
     2. 每个方法都要有一个兜底方法，代码量膨胀
        要区分统一的兜底方法和定制的兜底方法，并不可能每个方法都单独写一个对应的兜底方法(代码量过大)
      ```
        @DefaultProperties(defaultFallback="")注解
     1:1 每个方法都配置一个服务降级方法，技术上可以，但没人这么做
     1:N 除了个别重要核心业务有专属的服务降级方法，其他普通的可以通过@DefaultProperties(defaultFallback="")注解
         统一跳转到统一的服务降级方法上
     通用的和独享的各自分开，避免了代码膨胀，合理减少了代码量
     具体做法：(80客户端)
      1. 指定一个全局兜底方法-payment_Global_FallbackMethod
      2. 在类头上加上@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")   //设置全局兜底方法
      3. 在需要进行服务降级的方法上加上@HystrixCommand注解
     如果像之前一样指定了专门的服务降级方法，则走专门的方法，如果没有指定则走全局的兜底方法。
     ```  
     如果要解决代码耦合的问题，按照以下步骤执行：
     ```
        1. 修改cloud-consumer-feign-hystrix-order80，根据已有的PaymentHystrixService接口，新建一个类
          PaymentFallbackService实现该接口，重写接口方法，为接口里边的方法进行异常处理；
        2.yml文件配置
            feign:
               hystrix:
                 enabled: true
       3. 在PaymentHystrixService接口中添加注解@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
          
     ```     
     
## 服务熔断
   **熔断机制概述**
   熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，
   进而熔断该节点微服务的调用，快速返回错误的响应信息。
   当检测到该节点微服务调用响应正常后，恢复调用链路。
   在Spring Cloud框架里，熔断机制通过Hystrix实现。Hystrix会监控微服务间调用的状况，
   当失败的调用到一定阈值，缺省是5秒内20次调用失败，就会启动熔断机制。熔断机制的注解是@HystrixCommand。
   重点是这个注解：
   ```
        @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback",commandProperties = {
                @HystrixProperty(name = "circuitBreaker.enabled",value = "true"),// 是否开启断路器
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "10"),// 请求次数
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "10000"), // 时间窗口期
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "60"),// 失败率达到多少后跳闸
        })
        当服务有异常时先会服务降级-->当10次请求中错误率达到60%以上会导致熔断，-->在熔断窗口期过后会尝试接收请求，看是否能够成功，如果成功则恢复
        涉及到断路器的三个重要参数：快照时间窗，请求总阈值，错误百分比阈值
        1. 快照时间窗:断路器确定是沓打开需要统计一些请求和错误数据，而统计的时间范围就是快照时间窗，默认为最近的10秒.
        2. 请求总数阀值:在快照时间窗内，必须满足请求总数阀值才有资格熔断。默认为20，意味着在10秒内，
            如果该hystrix命令的调用次数不足20次，即使所有的请求都超时或其他原因失败，断路器都不会打开。
        3. 错误百分比阀值:当请求总数在快照时间窗内超过了阀值，比如发生了30次调用，如果在这30次调用中，
            有15次发生了超时异常，也就是超过50%的错误百分比，在默认设定50%阀值情况下，这时候就会将断路器打开。
           此时，再有请求调用的时候，将不会调用主逻辑，而是直接调用降级fallback,通过段路基实现了自动发现错误，并将降级逻辑切换
           为主逻辑，减少响应延迟的效果。
   ```
    熔断类型有三种:
    1. 熔断打开：请求不再进行调用当前服务，内部设置时钟一般为MTTR(平均故障处理时间)，当打开时长达到所设时钟则进入半熔断状态
    2. 熔断关闭：熔断关闭不会对服务进行熔断
    3. 熔断半开：部分请求根据规则调用当前服务，如果请求成功且符合规则，则认为当前服务恢复正常，关闭熔断
    
## Hystrix Dashboard
   除了隔离依赖服务的调用以外，Hystrix还提供了准实时的调用监控(Hystrix Dashboard)，
   Hystrix会持续地记录所有通过Hystrix发起的请求的执行信息，并以统计报表和图形的形式展示给用户，
   包括每秒执行多少请求多少成功，多少失败等。Netflix通过hystrix-metrics-event-stream项目实现了对以上指标的监控。
   Spring Cloud也提供了Hystrix Dashboard的整合，对监控内容转化成可视化界面。

