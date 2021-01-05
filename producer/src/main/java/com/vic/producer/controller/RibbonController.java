package com.vic.producer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("ribbon")
public class RibbonController {

    @Value("${cfg.ins:0}")
    private String instance;

    @GetMapping("test1")
    public String test1() {
        return "test1, instance " + instance;
     }

}
