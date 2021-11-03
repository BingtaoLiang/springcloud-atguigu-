##负载均衡(ribbon)
    Ribbon一句话概括就是:负载均衡+restTemplate

    新版本的Eureka客户端中自带了ribbon,因此不需要再额外引入ribbon的依赖
    
    Ribbon在工作时分成两步：
    1. 先选择EurekaServer,它优先选择在同一个区域内负载较少的server
    2. 再根据用户指定的策略，再从server取到的服务注册列表中选择一个地址，
       其中Ribbon提供了多种策略：比如轮询，随机和根据响应时间加权等。
       
## RestTemplate
   ### getForObject方法和getForEntity方法
       前者返回对象为响应体中数据转化成的对象，基本上可以理解为Json;
       后者返回对象为ResponseEntity对象，包含了响应中的一些重要信息，比如响应头，响应状态吗，响应体等
   
   
## Ribbon核心组件 IRule
   ## Ribbon自带的有7种选择策略
     1. BestAvailableRule ：选择一个最小的并发请求的server
     2. AvailabilityFilteringRule ：过滤掉那些因为一直连接失败的被标记为circuit tripped的后端server，并过滤掉那些高并发的的后端server（active connections 超过配置的阈值）
     3. WeightedResponseTimeRule ：根据相应时间分配一个weight，相应时间越长，weight越小，被选中的可能性越低。
     4. RetryRule ：对选定的负载均衡策略机上重试机制。
     5. RoundRobinRule ：roundRobin方式轮询选择server
     6. RandomRule ：随机选择一个server	
     7. ZoneAvoidanceRule ：复合判断server所在区域的性能和server的可用性选择server	
     
   ## 自定义选择策略
     官方文档明确的给出了警告：
     自定义的配置类不能放在@ComponentScan所扫描的当前包以及当前包的子包下（主启动类的@SpringBootApplication包含@ComponentScan
     因此自定义的配置类不能和主启动类放在同一包中，即不能放在com.neo.springcloud包下），否则，
     这个自定义的配置类就会被所有的Ribbon客户端所共享，达不到特殊化定制的目的了。
     
   步骤：
     1. 在主启动类所在包的外边新建自定义的策略类MyselfRule.class
     2. 在主启动类上添加@RibbonClient注解 
        @RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)
   
 # 默认负载均衡算法原理
   负载均衡算法：rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标，每次服务重启动后rest接口计数从1开始
   
   List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
     
   如： List [0] instances = 127.0.0.1:8002
       List [1] instances = 127.0.0.1:8001
       
   8001+ 8002组合成为集群，它们共计2台机器，集群总数为2，按照轮询算法原理:   
   
   当总请求数为1时: 1 % 2=1对应下标位置为1，则获得服务地址为127.0.0.1:8001
   当总请求数位2时: 2 % 2=0对应下标位置为0，则获得服务地址为127.0.0.1:8002
   当总请求数位3时: 3 % 2=1对应下标位置为1，则获得服务地址为127.0.0.1:8001
   当总请求数位4时: 4 % 2=0对应下标位置为0，则获得服务地址为127.0.0.1:8002
   如此类推.....
   
   