# 1. 概述
  # 1.1 分布式系统面临的配置问题
      微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现大量的服务。由于每个服务都需要必要的配置信息才能运行，所以一套集中式的、动态的配置管理设施是必不可少的。
      SpringCloud提供了ConfigServer来解决这个问题，我们每一个微服务自己带着一个application.yml，甚至包括开发环境，括测试环境，生产环境等等，上百个配置文件的管理..../八(ToT)/~~
  ## 1.2 是什么
    SpringCloud Config为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置。
  ## 1.3 怎么玩
    SpringCloud Config分为服务端和客户端两部分。
    服务端也称为分布式配置中心，它是一个独立的微服务应用，用来连接配置服务器并为客户端提供获取配置信息，加密/解密信息等访问接口
    客户端则是通过指定的配置中心来管理应用资源，以及与业务相关的配置内容，并在启动的时候从配置中心获取和加载配置信息。
    配置服务器默认采用git来存储配置信息，这样就有助于对环境配置进行版本管理，并且可以通过git客户端工具来方便的管理和访问配置内容
  ## 1.4 能干嘛
    1. 集中管理配置文件
    2. 不同环境不同配置，动态化的配置更新，分环境部署，比如dev/test/prod/beta/release
    3. 运行期间动态调整配置，不在需要在每个服务部署的机器上编写配置文件，服务会向配置中心统一拉去配置自己的信息
    4. 当配置发生变动时，服务不需要重启即可感知到配置的变化并应用新的配置
    5. 将配置信息以REST接口的形式暴露 --post/curl访问刷新均可
  ## 1.5 与Github整合配置
    由于SpringCloud Config默认使用git来存储配置文件(也有其他方式，比如支持SVN和本地文件)，
    但最推荐的还是git,而且实用的是http//https访问的形式 
    
# 2. Config服务端配置与测试
  ## 2.1 配置流程
    1. 用你自己的账号在GitHub上新建一个名为springcloud-config的新仓库
    2. 由上一步获得刚新建的git地址：git@github.com:BingtaoLiang/springcloud-config.git
    3. 本地硬盘目录上新建git仓库并clone 
       git本地地址：https://github.com/BingtaoLiang/springcloud-config.git
    4. 新建Module模块cloud-config-center3344,他即为cloud的配置中心模块 cloudConfig Center 
    5. pom
    6. yml
    7. 主启动类
    8. windows下修改hosts文件，增加映射   127.0.0.1     config-3344.com  
    9. 测试通过Config微服务是否可以从Github上获取配置内容
       尝试访问：http://config-3344.com:3344/main/config-dev.yml
  
  ## 2.2 配置读取规则
     掌握常用的几种即可
     1. /{label}/{application}-{profile}.yml   #推荐
        http://config-3344.com:3344/main/config-dev.yml  main分支
     2. /{application}-{profile}.yml
     3. /{application}/{profile}[/{label}]
     4. 重要配置细节总结
        label: 分支(branch)
        name: 服务名
        profiles: 环境(dev/test/prod)
   成功实现了用SpringCloud Config 通过github获取配置信息
   