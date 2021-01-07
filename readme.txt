
1. 测试feign默认超时时间
如果未配置feign.client.config.default相关，则connectTimeout默认10s，readTimeout默认60s
a) 关闭hystrix断路器开关，即feign.hystrix.enabled为false
b) 服务提供者代码
@GetMapping("bracket")
public String bracket() {
    try {
        // 当前线程等待61秒
        Thread.sleep(61000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "bracket";
 }
c) 服务调用端会显示错误信息
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.
Tue Dec 08 14:56:26 CST 2020
There was an unexpected error (type=Internal Server Error, status=500).
d) 修改全局feign参数
调用端配置如下：
feign:
  client:
    config:
      # feign默认配置，可指定服务名声明相关feign配置
      default:
        # feign接口默认连接超时配置
        connectTimeout: 10000
        # feign接口默认读取超时配置
        readTimeout: 8000
        loggerLevel: full

2. 测试feign超时触发重试机制
当发生超时时feign默认不会重试（参考feign.Retryer）。
如果需要重试，需要配置（参考com.vic.consumer.config.FeignRetryerConfig）

3. 测试对指定服务设置feign相关参数
a) 关闭hystrix断路器开关，即feign.hystrix.enabled为false
b) 调用端配置如下
feign:
  client:
    config:
      # 指定服务的feign配置
      producer1:
        # 连接超时时间
        connectTimeout: 5000
        # 数据处理超时时间
        readTimeout: 5000
        loggerLevel: full
c) 服务提供者如下
@GetMapping("maturity")
public String maturity() {
    try {
        Thread.sleep(6000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "maturity";
 }
d) 如果未找到某一参数配置，则取全局配置

4. 测试feign.hystrix.enabled
4.1 概念
hystrix断路器
在之前的监控数据中，我们看到过熔断器这个选项，熔断器也称断路器，是防止雪崩效应的一种方式。
hystrix自带断路器，断路器共有三种状态：Closed（关闭），Open（开启），Half Open（半开）
Closed（关闭）：默认状态下断路器是关闭的，此时所有的请求都可以访问。
Open（开启）：当请求次数大于20次，且这20次请求中有50%都失败的时候（可以自己设置），断路器就打开了。断路器打开后所有的请求都直接进入降级方法中
Half Open（半开）：当开启状态维持一段时间（默认5秒），就进入半开状态。此时会尝试释放一个请求调用微服务，如果释放的请求可以正常访问了，就关闭断路器，否则继续保持开启状态5秒。

通过断路器，可以防止请求在某一处堆积，造成雪崩效应。

4.2 测试
a) 调用端正确配置feign超时时间
feign:
  client:
    config:
      # feign默认配置
      default:
        # feign接口默认连接超时配置
        connectTimeout: 10000
        # feign接口默认读取超时配置
        readTimeout: 60000
b) 打开hystrix断路器开关
在调用端配置如下
feign:
  hystrix:
    # hystrix断路器开关
    # If true, an OpenFeign client will be wrapped with a Hystrix circuit breaker
    enabled: true
c) hystrix相关配置
在调用端配置如下
# hystrix相关配置
hystrix:
  command:
    default:
      execution:
        timeout:
          enabled: true
        isolation:
          thread:
            # hystrix的超时时间
            timeoutInMilliseconds: 8000
d) 服务提供端代码
@GetMapping("bracket")
public String bracket() {
    try {
        // 当前线程等待
        Thread.sleep(9000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return "bracket";
}
e) 当发生超时会走feign接口的fallback

f) 测试熔断打开
# hystrix.timeoutInMilliseconds   >=  ribbonTimeout = (ribbonReadTimeout + ribbonConnectTimeout) * (maxAutoRetries + 1) * (maxAutoRetriesNextServer + 1)
# hystrix的超时时间，开关在feign.hystrix.enabled
hystrix:
  command:
    # default全局有效，service id指定应用有效
    default:
      execution:
        timeout:
          # 如果enabled设置为false，则请求超时交给ribbon控制,为true,则超时作为熔断根据
          enabled: true
        isolation:
          thread:
            # 断路器超时时间，默认1000ms
            timeoutInMilliseconds: 10000
      circuitBreaker:
        # 触发熔断的最小请求次数，默认20次，当请求5次失败时，会触发熔断，即断路器打开
        requestVolumeThreshold: 5
        # 熔断多少秒后尝试请求，默认5秒
        sleepWindowInMilliseconds: 20000
        # 触发熔断的失败请求最小占比，默认50%
        errorThresholdPercentages: 50

g) 测试方法
依次启动eureka,consumer,producer
打开consumer的hystrix dashboard（参考6. 引入hystrix dashboard）


5. 测试ribbon
5.1 ribbon全局配置
a) 在consumer模块添加配置
# feign的负载均衡ribbon配置，spring cloud feign 默认开启支持ribbon，开关在spring.cloud.loadbalancer.ribbon.enabled配置
# ribbon的全局配置
ribbon:
  # ribbon请求连接实例的超时时间，默认值2000
  ConnectTimeout: 5000
  # 负载均衡超时处理时间，默认值5000
  ReadTimeout: 3000
  # 同一台实例最大重试次数,不包括首次调用，默认0
  MaxAutoRetries: 1
  # 重试负载均衡其他的实例最大重试次数,不包括首次调用，默认1
  MaxAutoRetriesNextServer: 1
  # 是否对所有操作都重试
  OkToRetryOnAllOperations: true

b) 测试（没试出来）
依次启动eureka,consumer。
启动producer，配置另一个实例，在Edit Configurations...中，复制一份Producer，并在environment variable中指定端口（cfg.ins=2;server.port=9002）。
或者新建一个producer1项目，服务名相同，端口不同。
在consumer模块中调整 com.vic.consumer.controller.RibbonController.t1

5.2 ribbon局部配置
#局部配置案例
ego-product-provider:
    ribbon:
        # 对所有操作请求都进行重试
        OkToRetryOnAllOperations=true
        # 对当前实例的重试次数
        MaxAutoRetries=2
        # 切换实例的重试次数
        MaxAutoRetriesNextServer=0
        # 请求连接的超时时间
        ConnectTimeout=3000
        # 请求处理的超时时间
        ReadTimeout=3000

6. 查看hystrix相关监控数据
在consumer模块中配置
a) 配置servlet
如果base-path也配置，以此为准
com.vic.consumer.config.ServletConfig
b) 配置路径
management:
  endpoints:
    web:
      exposure:
        include: "*"
      # 注意：和 com.vic.consumer.config.ServletConfig 指定要相同
      # base-path: /quorum
c) 访问 http://localhost:9006/hystrix.stream
出现ping:，访问一下其它路径即可（如：http://localhost:9006/hystrix/t1?id=2）

7. 引入hystrix dashboard
a) 创建hystrix-dashboard模块，并引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>

<!-- actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

b) 启动类添加 @EnableHystrixDashboard 注解
c) 配置 com.vic.hystrixdashboard.config.ServletConfig
d) 依次启动eureka,hystrix-dashboard,producer,consumer
在浏览器输入 http://localhost:9021/hystrix
在地址输入框中输入 http://localhost:9006/hystrix.stream (consumer服务)，点击 Monitor Stream
e) 访问feign接口，即可在图形化界面看到变化
f) 当hystrix监控界面出现 Unable to connect to Command Metric Stream
需要在hystrix-dashboard模块添加如下配置：
hystrix:
  # 把监控地址加入proxyStreamAllowList
  dashboard:
    proxy-stream-allow-list: "localhost"

8. 引入turbine
a) 创建turbine-service模块，并引入依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
<!--<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>-->

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-turbine</artifactId>
</dependency>

<!-- actuator -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>

b) 配置文件
server:
  port: 9011

spring:
  application:
    name: turbine-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

turbine:
  # 配置访问hystrix监控信息的前缀，与服务配置的路径有关
  instanceUrlSuffix: /hystrix.stream
  # 指定需要收集信息的服务名称，多个用,分隔
  app-config: consumer
  # 指定服务所属集群
  cluster-name-expression: new String('default')
  # 以主机名和端口号区分服务
  combine-host-port: true

c) 在启动类添加注解
@EnableTurbine

d) 测试
依次启动eureka,consumer,consumer1(复制实例并指定VM Options为-Dserver.port=9007),hystrix-dashboard,turbine-service
访问hystrix-dashboard模块：http://localhost:9021/hystrix
添加turbine-service服务的监控地址：http://localhost:9011/turbine.stream
点击 Monitor Stream
访问 http://localhost:9006/hystrix/t1?id=2, http://localhost:9007/hystrix/t1?id=2
可以看到Hosts为2（consumer,consumer1）。
新窗口访问 http://localhost:9006/hystrix2/t1?id=2，可以看到同一个Host两个hystrix方法（hystrixTest1,hystrix2Test1）
新窗口访问 http://localhost:9007/hystrix2/t1?id=2，可以看到两个图表，其中每个图表中Hosts为2。

Reference
// 优化feign组件
https://www.jianshu.com/p/59295c91dde7
https://www.jianshu.com/p/7a2c1c5d953d

// Hoxton.SR9版本使用hystrix dashboard
https://blog.csdn.net/haiertadiedie/article/details/110438819
// hystrix dashboard
https://www.haoyizebo.com/posts/56139c3d/