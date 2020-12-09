package com.vic.consumer.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign重试配置
 * @author 罗利华
 * date: 2020/12/8 19:12
 */
//@Configuration
public class FeignRetryerConfig {

    /**
     * 当feign超时时间触发时，这里的配置打开时feign会重试5次
     * @return
     */
    @Bean
    public Retryer feignRetryer() {
//        return new Retryer.Default();
        /**
         * 自定义重试
         */
        return new Retryer.Default(100, 1000, 4);
    }

}
