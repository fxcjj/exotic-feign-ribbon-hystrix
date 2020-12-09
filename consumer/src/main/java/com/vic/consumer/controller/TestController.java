package com.vic.consumer.controller;

import com.vic.commcommon.pojo.BoilerplateParam;
import com.vic.commserver.feign.producer.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:31
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    ProducerClient producerClient;

    @GetMapping("hello")
    public String hello(@RequestParam("name") String name) {
        return producerClient.hello(name);
    }

    @GetMapping("boilerplate")
    public String boilerplate() {
        BoilerplateParam param = new BoilerplateParam();
        param.setName("remediation");
        param.setAge(123);
        return producerClient.boilerplate(param);
    }


}
