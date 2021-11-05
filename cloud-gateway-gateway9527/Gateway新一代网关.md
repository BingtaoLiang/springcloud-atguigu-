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
   