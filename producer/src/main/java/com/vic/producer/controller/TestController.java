package com.vic.producer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("test")
public class TestController {

    /**
     * 在environment variables配置变量如下：
     * cfg.ins=2;server.port=9002
     */
    @Value("${cfg.ins:0}")
    private String instance;

    @GetMapping("hello")
    public String hello(@RequestParam("name") String name) {
        return "hello, " + name + ", current instance: " + instance;
     }
}
