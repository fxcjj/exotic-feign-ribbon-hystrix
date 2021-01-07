package com.vic.consumer.controller;

import com.vic.commserver.feign.producer.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2021/1/5 14:06
 */
@RestController
@RequestMapping("hystrix")
public class HystrixController {

    @Autowired
    ProducerClient producerClient;

    @Value("${spring.cloud.client.ip-address}")
    String ip;

    @GetMapping("t1")
    public String t1(@RequestParam("id") Integer id) {
        return producerClient.hystrixTest1(id) + "ip: " + ip;
    }


}
