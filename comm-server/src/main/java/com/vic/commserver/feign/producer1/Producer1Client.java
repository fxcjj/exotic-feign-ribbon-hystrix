package com.vic.commserver.feign.producer1;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 */
@FeignClient(name = "producer1", fallback = Producer1ClientHystrix.class)
public interface Producer1Client {

    /**
     * 测试超时
     * @return
     */
    @GetMapping("timeout/maturity")
    String maturity();
}

@Component
class Producer1ClientHystrix implements Producer1Client {

    @Override
    public String maturity() {
        return "maturity, 服务降级";
    }

}