package com.vic.producer1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("timeout")
public class TimeoutController {

    @GetMapping("maturity")
    public String maturity() {
        System.out.println("=========================================executing maturity method====================================");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "maturity";
     }

}
