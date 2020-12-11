
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


5. 有待测试ribbon

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

6. 引入hystrix dashboard
a) 引入依赖
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
c) 配置 com.vic.consumer.config.ServletConfig
d) 依次启动eureka,producer,consumer
在浏览器输入 http://localhost:9006/hystrix
在地址输入框中输入 http://localhost:9006/quorum/hystrix.stream，点击 Monitor Stream
e) 访问feign接口，即可在图形化界面看到变化
f) 当hystrix监控界面出现 Unable to connect to Command Metric Stream
需要在调用端配置如下：
hystrix:
  # 把监控地址加入proxyStreamAllowList
  dashboard:
    proxy-stream-allow-list: "localhost"


Reference
// 优化feign组件
https://www.jianshu.com/p/59295c91dde7
https://www.jianshu.com/p/7a2c1c5d953d

// Hoxton.SR9版本使用hystrix dashboard
https://blog.csdn.net/haiertadiedie/article/details/110438819
// hystrix dashboard
https://www.haoyizebo.com/posts/56139c3d/