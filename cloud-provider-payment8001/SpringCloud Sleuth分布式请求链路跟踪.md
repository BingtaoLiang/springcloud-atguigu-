#1. 概述
  问题：在微服务框架中，一个由客户端发起的请求在后端系统中会经过多个不同的的服务节点调用来协同产生最后的请求结果，每一个前段请求都会形成─条复杂的分布式服务调用链路，链路中的任何一环出现高延时或错误都会引起整个请求最后的失败
  
 ##1.1 是什么 
   Spring Cloud Sleuth提供了一套完整的服务跟踪的解决方案，在分布式系统中提供追踪解决方案并且兼容支持了zipkin

# 2. 搭建链路监控步骤
   Sleuth负责收集整理，zipkin负责展现。
   1. 下载zipkin
      SpringCloud从F版起已不需要自己构建ZipKin Server了，只需要调用jar包即可
      https://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/
   2. 进入zipkin的jar包所在目录，执行cmd命令`java -jar zipkin-server-2.12.9-exec.jar`启动zipkin
   3. 启动完成后，进入`http://localhost:9411/zipkin/` 查看zipkin的页面
   一条链路通过TraceId唯一标识，Span标识发起的请求信息，各Span通过ParentId关联起来
   Trace:类似于树结构的Span集合，标识一条调用链路，存在唯一标识
   Span:标识调用链路来源，通俗理解Span就是一次请求信息
   
   为了省事，这里直接在原来cloud-consumer-order80和cloud-provider-payment800模块进行修改
   - 首先8001模块修改pom
   ```html
         <!--包含了sleuth+zipkin-->
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-starter-zipkin</artifactId>
         </dependency>
   ```
   - 改yml
   ```yaml
          spring:
            application:
              name: cloud-payment-service
            zipkin:
                base-url: http://localhost:9411
              sleuth:
                sampler:
                #采样率值介于 0 到 1 之间，1 则表示全部采集
                probability: 1
   ```
   - 写controller
   
   - 80模块微服务的pom文件，yml文件修改和8001一样
   - 写controller
   
   依次启动7001/8001/80模块，调用几次服务，查看9411页面