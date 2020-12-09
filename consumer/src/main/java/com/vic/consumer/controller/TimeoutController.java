package com.vic.consumer.controller;

import com.vic.commserver.feign.producer.ProducerClient;
import com.vic.commserver.feign.producer.ProducerClient1;
import com.vic.commserver.feign.producer1.Producer1Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:31
 */
@RestController
@RequestMapping("timeout")
public class TimeoutController {

    @Autowired
    ProducerClient producerClient;

    @Autowired
    Producer1Client producer1Client;

    @GetMapping("bracket")
    public String bracket() {
        long startTime = System.currentTimeMillis();
        String bracket;
        try {
            bracket = producerClient.bracket();
        } catch (Exception e) {
            System.out.println("error bracket elapsed time: " + (System.currentTimeMillis() - startTime));
            return "error";
        }
        System.out.println("bracket elapsed time: " + (System.currentTimeMillis() - startTime));
        return bracket;
    }

    @GetMapping("maturity")
    public String maturity() {
        long startTime = System.currentTimeMillis();
        String maturity;
        try {
            maturity = producerClient.maturity();
        } catch (Exception e) {
            System.out.println("error maturity elapsed time: " + (System.currentTimeMillis() - startTime));
            return "error";
        }
        System.out.println("maturity elapsed time: " + (System.currentTimeMillis() - startTime));
        return maturity;
    }



}
