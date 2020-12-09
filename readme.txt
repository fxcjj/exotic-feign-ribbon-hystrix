
1. 测试feign.hystrix.enabled
在consumer模块中application.yml配置如下：
# feign相关配置
feign:
  client:
    config:
      # feign默认配置
      default:
        # feign接口默认连接超时配置
        connectTimeout: 5000
        # feign接口默认读取超时配置
        readTimeout: 7000
  hystrix:
    # hystrix断路器开关
    # If true, an OpenFeign client will be wrapped with a Hystrix circuit breaker
    enabled: false
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

调用者
com.vic.consumer.controller.TimeoutController.bracket

提供者



# feign相关配置
feign:
  hystrix:
    # If true, an OpenFeign client will be wrapped with a Hystrix circuit breaker
    enabled: false

当关闭hystrix时，不会返回执行fallback。

 * 如果产生超时
 * @return
 */
@GetMapping("bracket")
public String bracket() {
    long startTime = System.currentTimeMillis();
    String bracket = producerClient.bracket();
    System.out.println("elapsed time: " + (System.currentTimeMillis() - startTime));
    return bracket;
}





Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Tue Dec 08 14:56:26 CST 2020
There was an unexpected error (type=Internal Server Error, status=500).


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