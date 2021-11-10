#1. Nacos(Naming Configuration Service):服务注册和配置中心
     一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
     Nacos = Eureka + Config + Bus
     官网文档：nacos:https://nacos.io/zh-cn/
             Spring Cloud Alibaba:https://github.com/alibaba/spring-cloud-alibaba/blob/master/README-zh.md
             Spring Cloud Alibaba手册:https://spring-cloud-alibaba-group.github.io/github-pages/hoxton/en-us/index.html#_dependency_management
             
 ##1.1 Spring Cloud Alibaba
     主要功能：
     - 服务限流降级：默认支持 WebServlet、WebFlux、OpenFeign、RestTemplate、Spring Cloud Gateway、Zuul、Dubbo 和 RocketMQ 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
     - 服务注册与发现：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
     - 分布式配置管理：支持分布式系统中的外部化配置，配置更改时自动刷新。
     - 消息驱动能力：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
     - 分布式事务：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。
     - 阿里云对象存储：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
     - 分布式任务调度：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
     - 阿里云短信服务：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。
     -----------------------------------------------------------------------------------------------------------

     主要组件：
     - Sentinel：把流量作为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。
     - Nacos：一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。
     - RocketMQ：一款开源的分布式消息系统，基于高可用分布式集群技术，提供低延时的、高可靠的消息发布与订阅服务。
     - Dubbo：Apache Dubbo™ 是一款高性能 Java RPC 框架。
     - Seata：阿里巴巴开源产品，一个易于使用的高性能微服务分布式事务解决方案。
     - Alibaba Cloud OSS: 阿里云对象存储服务（Object Storage Service，简称 OSS），是阿里云提供的海量、安全、低成本、高可靠的云存储服务。您可以在任何应用、任何时间、任何地点存储和访问任意类型的数据。 
     - Alibaba Cloud SchedulerX: 阿里中间件团队开发的一款分布式任务调度产品，提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。
     - Alibaba Cloud SMS: 覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。
       更多组件请参考 Roadmap。
       
 ##1.2 Nacos下载与安装
     下载：https://github.com/alibaba/nacos/tags
     解压安装包，直接运行bin目录下的startup.cmd -m standalone  单机模式启动
     命令运行成功后直接访问http://localhost:8848/nacos，默认账号和密码都是nacos
     
 ##1.3 新建module(参照官网)
   1. 新建module cloudalibaba-provider-payment9001 服务提供者
   2. 父pom添加依赖
      ```html
           <dependency>
               <groupId>com.alibaba.cloud</groupId>
               <artifactId>spring-cloud-alibaba-dependencies</artifactId>
               <version>2.2.6.RELEASE</version>
               <type>pom</type>
               <scope>import</scope>
           </dependency>
      ```
      子pom添加依赖
      ```html
           <!--SpringCloud ailibaba nacos -->
           <dependency>
               <groupId>com.alibaba.cloud</groupId>
               <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
           </dependency>
      ```
   3. yml文件
      ```yaml
             server:
               port: 9001
             
             spring:
               application:
                  name: nacos-payment-provider
               cloud:
                 nacos:
                   discovery:
                     server-addr: localhost:8848  #Nacos注册中心地址
             
             management:
               endpoints:
                 web:
                   exposure:
                     include: '*'
      ```
   4. 启动类，业务类
   5. 测试 
      - 启动8848Nacos注册中心
      - 启动9001微服务
      - 到Nacos8848页面可以看到注册进的微服务
   6. 为了下一章演示nacos的负载均衡，参照9001新建9002，或者不想新建重复体力劳动，直接拷贝虚拟端口映射
      在Services中“Running”里的9001微服务，右键选择"Copy Configuration" 新起一个名字，并在-Vm Option中添加`-DServer.port=9011`
      便可在"Not Started"中看到这个虚拟的应用"PaymentMain9011 CopyOf9001"
   7. 这里我们新建了9002模块
   
 ##1.4 新建服务消费者-83模块
   1. 新建cloudalibaba-consumer-nacos-order83
   2. 改pom
   3. 改yml
   4. 主启动
   5. 业务类(ApplicationContextConfig和OrderNacosController)
   6. 测试，启动Nacos,9001,9002,83模块
      访问http://localhost:83/consumer/payment/nacos/12
      依次访问9001/9002
      Nacos自带负载均衡，整合了ribbon
   
 ##1.5 Nacos作为服务注册中心对比
   Nacos同时支持CP和AP，可以自由切换 
   
   C是所有节点在同一时间看到的数据是一致的;而A的定义是所有的请求都会收到响应。
   何时选择使用何种模式?
   一般来说，如果不需要存储服务级别的信息且服务实例是通过nacos-cient注册，并能够保持心跳上报，那么就可以选择AP模式。
   当前主流的服务如Spring cloud和Dubbo服务，都适用于AP模式，AP模式为了服务的可能性而减弱了一致性，因此AP模式下只支持注册临时实例。
   如果需要在服务级别编辑或者存储配置信息，那么CP是必须，K8S服务和DNS服务则适用于CP模式。
   CP模式下则支持注册持久化实例，此时则是以Raft协议为集群运行模式，该模式下注册实例之前必须先注册服务，如果服务不存在，则会返回错误.
   
   curx -X PUT''$NACOS_SERVER:8848/nacos/v1/ns/operator/switches?entry=serverMode&value=CP'




#2. Nacos做服务配置中心演示
 ##2.1 Nacos作为配置中心--基础配置
   - 新建cloudalibaba-config-nacos-client3377
   - pom
   - 两个yml(application.yml和bootstrap.yml)
     为什么是两个yml?
     Nacos同springcloud-config一样，在项目初始化时，要保证先从配置中心进行配置拉取,拉取配置之后，才能保证项目的正常启动。
     springboot中配置文件的加载是存在优先级顺序的，bootstrap优先级高于application

   - 主启动
   - 业务类 加上@Refresh注解开启动态刷新功能
   - 在Nacos中添加配置信息
     Nacos中的匹配规则，理论：Nacos中的dataid的组成格式及与SpringBoot配置文件中的匹配规则
     公式如下：${prefix}-${spring.profiles.active}.${file-extension}
     1. `prefix` 默认为 `spring.application.name` 的值，也可以通过配置项 `spring.cloud.nacos.config.prefix`来配置
     2. `spring.profiles.active` 即为当前环境对应的 `profile`，详情可以参考 Spring Boot文档。 
        注意：当 `spring.profiles.active` 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 `${prefix}.${file-extension}`
     3. `file-exetension` 为配置内容的数据格式，可以通|过配置项 `spring.cloud.nacos.config.file-extension` 来配置。目前只支持 properties 和 yaml 类型。
     即，官方公式可以写成下面的样子：
     ```
         ${spring.application.name}-${spring.profile.active}.${spring.cloud.nacos.config.file-extension}
         如：nacos-config-client-dev.yaml //要注意文件后缀必须为`yaml`不能是`yml`
     ```
   - 测试
     在测试前需要在nacos客户端-配置管理-配置管理栏目下有对应的yaml配置文件
     运行cloud-config-nacos-client3377的主启动类
     调用接口查看配置信息 
   
   - 自带动态刷新
     修改Nacos中的yaml配置文件，再次调用，查看配置的接口，就会发现配置已经刷新  

 ##2.2 Nacos作为配置中心-分类配置 -Nacos之命名空间分组和DataId三者之间的关系    
   问题1:
      实际开发中，通常一个系统会准备
      dev开发环境
      test测试环境
      prod生产环境。
      如何保证指定环境启动时服务能正确读取到Nacos上相应环境的配置文件呢?
   问题2:
      一个大型分布式微服务系统会有很多微服务子项目，
      每个微服务项目又都会有相应的开发环境、测试环境、预发环境、正式环境.....
      那怎么对这些微服务配置进行管理呢?
   
   Nacos是按照`Namespace+Group+DataId`设计，三者之间什么关系，为什么这么设置? 
   ```html
        三者类似于Java里面的package名和类名
        最外层的namespace是可以用于区分部署环境的，Group和DataId逻辑上区分两个目标对象
     默认情况:
        Namespace=public，Group=DEFAULT_GROUP,默认Cluster是DEFAULT

        Nacos默认的命名空间是public，Namespace主要用来实现隔离。
        比方说我们现在有三个环境:开发、测试、生产环境，我们就可以创建三个Namespace，不同的Namespace之间是隔离的。

        Group默认是DEFAULT_GROUP，Group可以把不同的微服务划分到同一个分组里面去

        Service就是微服务;一个Service可以包含多个Cluster (集群)，Nacos默认Cluster是DEFAULT，Cluster是对指定微服务的
        一个虚拟划分。
        比方说为了容灾，将Service微服务分别部署在了杭州机房和广州机房，
        这时就可以给杭州机房的Service微服务起一个集群名称(HZ)，
        给广州机房的Service微服务起一个集群名称(GZ)，还可以尽量让同一个机房的微服务互相调用，以提升性能。
        最后是Instance，就是微服务的实例。

   ```
   **三种方案加载配置：**
   - DataId方案
     ```html
        指定spring.profile.active和配置文件的DataId来使不同环境下读取不同的配置
        默认空间+默认分组+新建dev和test两个DataId
            在nacos页面新建配置：
            nacos-config-client-dev.yaml
            nacos-config-client-test.yaml
        通过spring.profile.active属性就能进行多环境下配置文件的读取，你配的是dev环境就加在dev的配置，配的什么就加载什么环境
     ```
   - Group方案
     ```html
        通过Group实现环境区分，默认是default_Group，
           新建Group:
           nacos-config-client-info.yaml  DEV_GROUP
           nacos-config-client-info.yaml  TEST_GROUP
           DataId一样，Group不一样
        在nacos图形界面控制台上新建配置文件DataId
        bootstrap+application
            在config下增加一条group的配置即可，可配置为DEV_GROUP或TEST_GROUP
     ```  
   - Namespace方案
     ```html
        新建dev/test的Namespace
        回到服务管理-服务列表查看
           服务列表|dev 1c509993-b955-4c83-9b60-ef8e9cecc76c
           服务列表|test 49e78943-4006-448b-a3e5-a8fb49c97abc
        按照域名配置填写(以dev命名空间为例)
           在dev命名空间下新建不同的group和dataId,然后在bootstrap.yml中通过namespace: 1c509993-b955-4c83-9b60-ef8e9cecc76c和group，以及application.yaml中的active选择加载不同的配置环境
     ```
   
 ##2.3 Nacos集群和持久化配置(重要！)    
   官网：https://nacos.io/zh-cn/docs/cluster-mode-quick-start.html
   按照官网上的说明： DNS--->VIP(虚拟Ip映射，也就是Nginx集群)--->Nacos集群--->MySQL集群(主备模式，存储数据)
   默认Nacos使用嵌入式数据库实现数据的存储。所以，如果启动多个默认配置下的Nacos节点，数据存储是存在一致性问题的为了解决这个问题，Nacos采用了集中式存储的方式来支持集群化部署，目前只支持MySQL的存储。
   Nacos支持三种部署模式
   ·单机模式-用于测试和单机试用。
   ·集群模式-用于生产环境，确保高可用。
   ·多集群模式-用于多数据中心场景。
   **Windows**
   cmd startup.cmd或者双击startup.cmd 文件
   **单机模式支持mysql**
   在0.7版本之前，在单机模式时nacos使用嵌入式数据库实现数据的存储，不方便观察数据存储的基本情况。
   0.7版本增加了支持mysql数据源能力，具体的操作步骤:
    ·1.安装数据库，版本要求: 5.6.5+
    ·2.初始化mysql数据库，数据库初始化文件: nacos-mysql.sql
    ·3.修改conf/application.properties文件，增加支持mysql数据源配置（目前只支持mysql)，添加mysql数据源的ur用户名和密码。
    ```
       spring.datasource.platform=mysql
       db.num=1
       db.url.O-jdbc:mysql://11.162.196.16:3306/nacos_devtest?characterEncoding-utf8&connectTimeout-10ecdb.user=nacos_devtest
       db.password=youdontknow
    ```
    再以单机模式启动nacos,nacos所有写入嵌入式数据库的数据都写入到了mysql。
  ###2.3.1 Nacos持久化配置解释
     Nacos默认自带的嵌入式数据库时derby   
     deyby到mysql切换配置的步骤 
       1. nacos安装目录下的conf目录下找到sql脚本，nacos-mysql.sql,复制内容，到mysql中执行    
       2. nacos目录下的conf目录下找到application.properties，修改其中的数据库连接
          #####################################更改内嵌式数据库derby为mysql###########################################################
          spring.datasource.platform=mysql
          
          db.num=1
          db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&serverTimezone=UTC
          db.user.0=root
          db.password.0=root
       3. 重启Nacos,可以看到8848页面是个全新的空记录界面，以前是记录到derby
  
  ###2.3.2 Linux版Nacos + MySQL生产环境配置
        linux服务器下/usr/local目录下新建了个 myNacos目录存放nacos
    1. 预计需要1个Nginx+3个nacos注册中心+1个mysql
    2. Nacos下载Linux版 解压在/opt目录下
    3. 集群配置步骤（重点）
       如果是单nacos 启动默认就是8848端口
       如果在linux上部署nacos集群，我们需要改一下linux中nacos的启动脚本，让我们可以传递端口参数，按端口启动nacos
     3.1 Linux服务器上部署mysql数据库配置，将nacos-mysql.sql中的脚本在linux服务器上的数据库中执行一遍  
     3.2 application.propertes配置，修改其中的数据库连接
     3.3 Linux服务器上nacos的集群配置cluster.conf ，梳理出3台nacos机器的不同服务端口号，复制出cluster.conf,内容参照"192.168.16.101:8847" 其中ip必须是linux命令"hostname -i"能够识别的ip
    4. 编辑Nacos的启动脚本startup.sh，使它能够接受不同的启动端口 
     4.1 myNacos/bin目录下有startup.sh
     4.2 在什么地方，修改什么，怎么修改
     4.3 思考
         集群启动时，我们希望可以类似其他软件的shell命令，传递不同的端口号启动不同的nacos实例。
         ./startup.sh -p3333 表示启动端口号为3333的nacos服务器实例，和上一步的cluster.conf配置的一致
     4.4 修改内容
     4.5 执行方式
     
     ps:太卡了跑不成
   