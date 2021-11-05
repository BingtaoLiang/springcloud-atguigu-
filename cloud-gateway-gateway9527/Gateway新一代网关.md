# 1. Gateway概述简介
    Gateway是在Spring生态系统之上构建的API网关服务，基于Spring 5,Spring Boot 2和Project Reactor等技术。
    Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如:熔断、限流、重试等
---------------------------------------------------------------------------------------------------
    SpringCloud Gateway是Spring Cloud的一个全新项目，基于Spring5.0+SpringBoot2.0和Project Reactor等技术开发的网关，它旨在为微服务架构提供—种简单有效的统一的API路由管理方式。
    SpringCloud Gateway作为Spring Cloud生态系统中的网关，目标是替代Zuul，在SpringCloud 2.0以上版本中，没有对新版本的Zuul 2.0以上最新高性能版本进行集成，仍然还是使用的Zul 1.x非Reactor模式的老版本.
    而为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty。
    Spring Cloud Gateway的目标提供统一的路由方式且基于Fiter链的方式提供了网关基本的功能，例如:安全，监控/指标，和限流。
  一句话：SpringCloud Gateway使用的是Webflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架。
  能干嘛：反向代理、鉴权、流量控制、熔断、日志监控... 
  SpringCloud Gateway具有如下特性：
   1. 基于Spring Framework 5, Project Reactor 和Springboot2.0 进行构建；
   2. 动态路由:能够匹配任何请求属性;
   3. 可以对路由指定Predicate(断言)和Filter (过滤器);集成Hystrix的断路器功能;
   4. 集成Spring Cloud 服务发现功能;
   5. 易于编写的Predicate(断言）和Filter (过滤器);请求限流功能;
   6. 支持路径重写。
 
# 2.三大核心概念
  1. Route(路由)：路由是构建网关的基本模块，由ID，目标URI,一系列的断言和过滤器组成，如果断言为true则匹配该路由
  2. Predicate(断言)：参考的是Java8的java.util.function.Predicate,开发人员可以匹配HTTP请求中的所有内容，
                    例如请求头或参数，如果请求与断言相匹配则进行路由
  3. Filter(过滤)：指的是Spring框架中GatewayFilter的实例，使用过滤器可以在请求被路由前或者后对请求进行修改
  4. 总体：web请求通过一些匹配条件，定位到真正的服务节点，并在这个转发过程的前后，进行一些精细化控制，
       predicate就是我们的匹配条件，而Filter就可以理解为一个无所不能的拦截器，有了这两个元素，再加上目标URI，就可以实现一个具体的路由了
       
       
       
# 3.Gateway工作流程
   1. 客户端向Spring Cloud Gateway发出请求。然后在Gateway Handler Mapping 中找到与请求相匹配的路由，将其发送到GatewayWeb Handler。
   2. Handler再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回。
   3. 过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前("pre")或之后("post"）执行业务逻辑。
   4. Filter在"pre"类型的过滤器可以做参数校验、权限校验、流量监控、日志输出、协议转换等，
   5. 在"post"类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等有着非常重要的作用。
  
  - 核心逻辑：路由转发+执行过滤器链
  
  
# 4.Gateway网关如何做路由映射呢？
  1. 选两个服务`cloud-provider-payment8001`看看controller的访问地址  get/{id}和lb
  2. 我们目前不想暴露8001端口，希望在8001外面套一层9527，通过9527端口将请求转发到8001服务侧
  **解决办法--yml中新增网关配置**
  ```yaml
         spring:
           application:
             name: cloud-gateway
         `#路由网关配置`
           cloud:
             gateway:
               routes:
                 - id: payment_routh #payment_route  #路由的ID，没有固定的规则但要求唯一，建议配合服务名
                   uri: http://localhost:8001    #匹配后提供服务的路由地址
                   predicates:
                     - Path=/payment/get/**    #断言，路径相匹配的进行路由
         
                 - id: payment_routh2   #payment_route  #路由的ID，没有固定的规则但要求唯一，建议配合服务名
                   uri: http://localhost:8001    #匹配后提供服务的路由地址
                   predicates:
                     - Path=/payment/lb/**    #断言，路径相匹配的进行路由
                 # 通过9527网关跳转访问百度新闻
                 - id: payment_routh3   #payment_route  #路由的ID，没有固定的规则但要求唯一，建议配合服务名
                   uri: http://news.baidu.com    #匹配后提供服务的路由地址
                   predicates:
                     - Path=/**    #断言，路径相匹配的进行路由
  
  ```
   
   完成之后通过`http://localhost:8001/payment/get/1`
   和`http://localhost:9527/payment/get/1` 都可以完成访问
   
   
 ## 4.1 Gateway网关路由有两种配置方式
   1. 在配置文件yml中配置 具体见第4节
   2. 代码中注入RouteLocator的Bean 相较于yml配置方式太繁琐，推荐使用yml配置的方式，直观简单
   
 ## 4.2 通过微服务名实现动态路由
   默认情况下gateway会根据注册中心注册的服务列表，以注册中心上微服务名为路径创建动态路由进行转发，从而实现动态路由的功能
   ```yaml
      #主要是以下配置
     spring: 
       cloud:
          gateway:
            discovery:
              locator:
                enabled: true   #开启从注册中心动态创建路由的功能，利用微服务明进行动态路由      
            routes:
              - id: payment_routh #payment_route  #路由的ID，没有固定的规则但要求唯一，建议配合服务名
              #          uri: http://localhost:8001    #匹配后提供服务的路由地址
                uri: lb://cloud-payment-service #匹配后提供服务的路由地址(动态路由)

   ```
   
# 5. Gateway常用的Predicates
 常用的Route Predicate 
 
     1. Loaded RoutePredicateFactory [After]
        要获得对应格式的时间串，利用如下代码获得：
        public static void main(String[] args) {
                ZonedDateTime zonedDateTime = ZonedDateTime.now();
                System.out.println(zonedDateTime);
                //2021-11-05T09:35:54.329+08:00[Asia/Shanghai]
        }
        2021-11-05T09:35:54.329+08:00[Asia/Shanghai]
     2. Loaded RoutePredicateFactory [Before]
            Before和Between的用法和After类似
     3. Loaded RoutePredicateFactory [Between]
            - Between=2021-11-06T09:35:54.329+08:00[Asia/Shanghai],2021-11-07T09:35:54.329+08:00[Asia/Shanghai] #在这两个时间之间的请求才生效
     4. Loaded RoutePredicateFactory [Cookie]
            Cookie Route Predicate 需要两个参数，一个是Cookie name,一个是正则表达式
            路由规则会通过获取对应的Cookie name值和正则表达式去匹配，如果匹配上就会执行路由，没有匹配上就不执行
            在cmd窗口执行`curl http://localhost:9527/payment/get/1`因为没有带上yml文件中配置的cookie，所以会请求失败
            如果执行`curl http://localhost:9527/payment/get/1 --cookie "username=lbt"`就会请求成功
     5. Loaded RoutePredicateFactory [Header]
            两个参数：一个是属性名称和一个正则表达式，这个属性值和正则表达式匹配则执行 
             curl http://localhost:9527/payment/get/1 -H "X-Request-Id:123"会执行成功
     6. Loaded RoutePredicateFactory [Host]
            Host Route Predicate 接收一组参数，一组匹配的域名列表，这个模板是一个ant分割的模板，用.号作为分割符，它通过参数中的主机地址作为匹配规则
     7. Loaded RoutePredicateFactory [Method]
            -Method=GET
     8. Loaded RoutePredicateFactory [Path]
     9. Loaded RoutePredicateFactory [Query]]
            -Query=username, \d+ #要有参数名username并且值还要是整数才能路由
     10. Loaded RoutePredicateFactory [ReadBodyPredicateFactory]
     11. Loaded RoutePredicateFactory [RemoteAddr]
      
 这些内置的Route Predicate都与HTTP请求的不同属性匹配，多个Route Predicate工厂可以进行组合。
 
 # 6. Filter
    路由过滤器可用于修改进入的HTTP请求和返回的HTTP相应，路由过滤器只能指定路由进行使用。
    Spring Cloud Gateway内置了多种路由过滤器，他们都由GatewayFilter的工厂类来产生
    
  生命周期:
   1. pre
   2. post
  种类：
   1. GatewayFilter  31种
   2. GlobalFilter   10余种
   
   **自定义过滤器(使用更多一些)**
   1. 两个主要接口介绍： implemnts GlobalFilter,Ordered
   2. 能干嘛，在所有的微服务前边挡着，可以做全局日志记录，统一网关鉴权等
   3. 代码见`MyLogGatewayFilter.class`
   4. 测试：
      正确：http://localhost:9527/payment/lb?uname=zs
      错误：http://localhost:9527/payment/lb
   