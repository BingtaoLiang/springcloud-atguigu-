# 监控配置流程
  1. 新建module
  2. 改pom文件，主要是下面这个
   ``` 
         <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
         </dependency>
   ```
   3. 改yml 配置端口号9001
   4. 主启动类，添加新注解@EnableHystrixDashboard 
   5. 所有微服务提供类(8001/8002/8003)都要监控依赖配置
   ```
          <!--actuator监控信息-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
   ```
   6. 启动9001服务，该微服务后序将监控微服务8001
   
   
# 避坑指南
  1. 要监控8001 需要对8001微服务的主启动类进行一个配置：
   ```
        /**
         *此配置是为了服务监控而配置，与服务容错本身无关，springcloud升级后的坑
         *ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
         *只要在自己的项目里配置上下面的servlet就可以了
        */
        @Bean
        public ServletRegistrationBean getServlet() {
            HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
            ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
            registrationBean.setLoadOnStartup(1);
            registrationBean.addUrlMappings("/hystrix.stream");
            registrationBean.setName("HystrixMetricsStreamServlet");
            return registrationBean;
        }
   ```
  2. 在9001监控页面填写监控地址
     http://localhost:8001/hystrix.stream
     http://localhost:8001/hystrix.stream  
     Delay: 2000ms 
     分别调用正确和错误的地址：
     http://localhost:8001/payment/circuit/1
     http://localhost:8001/payment/circuit/-2