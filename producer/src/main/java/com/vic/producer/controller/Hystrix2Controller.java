package com.vic.producer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("hystrix2")
public class Hystrix2Controller {

    @GetMapping("test1")
    public String test1(@RequestParam("id") Integer id) {
        if(id != 1) {
            throw new RuntimeException("not 1");
        }
        return "test1";
     }

}
