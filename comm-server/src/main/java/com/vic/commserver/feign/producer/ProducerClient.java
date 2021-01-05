package com.vic.commserver.feign.producer;

import com.vic.commcommon.pojo.BoilerplateParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 */
@FeignClient(name = "producer", fallback = ProducerClientHystrix.class)
public interface ProducerClient {

    @GetMapping("ribbon/test1")
    String ribbonTest1();


    @GetMapping("test/hello")
    String hello(@RequestParam(value = "name") String name);

    /**
     * 测试超时
     * @return
     */
    @GetMapping("timeout/bracket")
    String bracket();

    /**
     * 测试超时
     * @return
     */
    @GetMapping("timeout/maturity")
    String maturity();

    /**
     * @SpringQueryMap 可以
     * @param param
     * @return
     */
    @GetMapping("merit/boilerplate")
    String boilerplate(@SpringQueryMap BoilerplateParam param);

}

/**
 * access to the cause that made the fallback trigger
 */
@Component
class ProducerClientHystrix implements ProducerClient {

    @Override
    public String ribbonTest1() {
        return "ribbonTest1, 服务降级";
    }

    @Override
    public String hello(String name) {
        return "hello, 服务降级";
    }

    @Override
    public String bracket() {
        return "bracket, 服务降级";
    }

    @Override
    public String maturity() {
        return "maturity, 服务降级";
    }

    @Override
    public String boilerplate(BoilerplateParam param) {
        return "boilerplate, 服务降级";
    }

}