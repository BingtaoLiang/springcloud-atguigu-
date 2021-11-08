# 1. 流程
   1. 建module
   2. 改pom，这里注意
   ```yaml
      <dependency>
            <groupId>org.springframework.cloud</groupId>
            <!--和3344项目不同，不带server，表示是客户端<artifactId>spring-cloud-config-server</artifactId>-->
            <artifactId>spring-cloud-starter-config</artifactId>
            <version>2.0.2.RELEASE</version>
        </dependency>
   ```
  3. **改yml--bootstrap.yml**
   ```text
      applicaiton.yml是用户级别的资源配置项，而bootstrap.yml是系统级的，优先级更加高
      Spring Cloud会创建一个“Bootstrap Context”，作为Spring应用的'Application Context的父上下文。
      初始化的时候，'Bootstrap Context'负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的`Environment`。

      Bootstrap属性有高优先级，默认情况下，它们不会被本地配置覆盖。‘Bootstrap context和Application Context有着不同的约定，
      所以新增了一个'bootstrap.yml'文件，保证`Bootstrap Context和`Application Context'配置的分离。

      要将Client模块下的application.yml文件改为bootstrap.yml,这是很关键的，
      因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml

      客户端带着两份配置文件，一份bootstrap.yml一份application.yml，前者用于从配置中心获取公共的配置，后者用于自身私有的配置
   ```
  4. 修改config-dev.yml配置并提交到github中，比如加个变量age或者版本号version
  5. 主启动类
  6. 业务类，获取github仓库中的配置信息
  7. 测试
     启动Config配置中心3344微服务并自测：http://config-3344.com:3344/main/config-prod.yml
     启动3355微服务并自测：http://localhost:3355/configInfo 得到结果：master branch,springcloud-config/config-dev.yml version=8
  
  成功实现了客户端3355访问springcloud Config3344通过github获取配置信息
     
# 2. 可是问题随之而来，分布式配置的动态刷新问题（重点难点）
    Linux运维修改GitHub上的配置文件内容做调整
    刷新3344，发现ConfigServer配置中心立刻相应
    刷新3355，发现ConfigClient客户端没有任何相应
    3355没有变化除非自己重启或重新加载
    难道每次运维修改配置文件，客户端都要重启？？噩梦
  ## 2.1 目的：避免每次更新github配置文件都要重启客户端微服务3355/3366/...
  ## 2.2 动态刷新步骤：
       1. 修改3355模块，pom引入actuator监控
       2. 修改yml，暴露监控接口
       3. 业务类controller修改  添加@RefreshScope注解
       4. 此时修改github配置文件-->3344--->3355  发现3355还是没有或得到最新版本的配置文件
       
       如何解决呢？
       需要运维人员发送post请求刷新3355，必须是post请求
       curl -X POST "http://localhost:3355/actuator/refresh"
       
       之后客户端再次请求http://localhost:3355/configInfo  
       发现成功实现了客户端3355刷新到最新配置内容，避免了服务重启
     
  ## 2.3 想想还有什么问题
       加入有多个微服务客户端3355/3366/3377...
       每个微服务都要执行一次post请求，手动刷新？？？
       能否广播，一次通知，处处生效
       我们想大范围的自动刷新，求方法~~~