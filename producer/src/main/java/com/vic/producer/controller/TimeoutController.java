package com.vic.producer.controller;

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

    @GetMapping("bracket")
    public String bracket() {
        System.out.println("=========================================executing bracket method====================================");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "bracket";
     }

}
