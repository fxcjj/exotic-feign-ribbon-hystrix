package com.vic.consumer;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Haxton.SR9版本加 @EnableHystrix 会提示错误如下：
 * Caused by: java.lang.ClassNotFoundException: com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect
 */
//@EnableHystrix
//@EnableCircuitBreaker
@EnableFeignClients(basePackages = {"com.vic.commserver.feign"})
/**
 * 注意：这里如果只是扫描com.vic.commserver是不行的，还需要扫描自己项目，可配置上级目录，即：com.vic
 */
@ComponentScan(basePackages = {"com.vic"})
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
