package com.vic.commserver.feign.producer;

import feign.hystrix.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 */
@FeignClient(name = "producer", fallbackFactory = ProducerFallbackFactory.class)
public interface ProducerClient1 {

    @GetMapping("merit/getCause")
    String getCause(@RequestParam(value = "name", required = false) String name);

}

/**
 * access to the cause that made the fallback trigger
 */
@Component
class ProducerFallbackFactory implements FallbackFactory<ProducerClient1> {

    @Override
    public ProducerClient1 create(Throwable throwable) {
        return new ProducerClient1() {
            @Override
            public String getCause(String name) {
                return "fallbackFactory, reason was: " + throwable.getMessage();
            }
        };
    }
}