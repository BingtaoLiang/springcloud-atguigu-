#1. Sentinel规则持久化
     以8401模块进行演示
    - 是什么: 一旦我们重启应用，sentinel规则将消失，生产环境需要将配置规则进行持久化
    - 怎么玩: 将限流配置规则持久化进Nacos保存，只要刷新8401某个rest地址，sentinel控制台的流控规则就能看到，
             只要Nacos里边的配置不删除，针对8401上sentinel上的流控规则就持续有效
    - 步骤:  
          1. 修改cloudalibaba-sentinel-service8401
          2. pom pom中添加如下依赖
             ```
              <!--SpringCloud ailibaba sentinel-datasource-nacos 后续做持久化用到-->
                    <dependency>
                        <groupId>com.alibaba.csp</groupId>
                        <artifactId>sentinel-datasource-nacos</artifactId>
                    </dependency>
             ```
          3. yaml
             ```
             sentinel:
                   transport:
                     dashboard: localhost:8080 #配置Sentinel dashboard地址
                     #默认8719端口，假如被占用会自动从8719开始依次+1扫描，直至找到未被占用的端口
                     port: 8719
                   datasource:
                     ds1:
                       nacos:
                         server-addr: localhost:8848
                         dataId: cloudalibaba-sentinel-service
                         groupId: DEFAULT_GROUP
                         data-type: json
                         rule-type: flow
             ```
          4. 添加Nacos业务规则配置，按照yaml文件中的设置进行配置
             配置内容：选择json格式
             ```
             [
               {
                 "resource":"/rateLimit/byUrl",
                 "limitApp":"default",
                 "grade":1,
                 "count":1,
                 "stratey":0,
                 "controlBehavior":0,
                 "clusterMode":false
               }
             ]
             
             resources:资源名称
             limitApp:来源应用
             grade:阈值类型，0表示线程数，1表示QPS;
             count:单机阈值
             stragegy:流控模式，0表示直接，1表示关联，2表示链路;
             controlBehavior:流控效果，0表示快速失败，1表示warm up，2表示排队等待
             clusterMode:是否集群
             ```
          5. 启动8401模块，发现刷新sentinel已经有了业务规则   
          6. 快速访问测试接口 http://localhost:8401/rateLimit/byUrl，快速刷新可以看到默认的降级消息，blocked...
          7. 停止8401之后再看sentinel发现没有规则了
          8。 重启8401再看sentinel发现又出现了配置过的规则
             