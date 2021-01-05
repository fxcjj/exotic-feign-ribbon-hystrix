package com.vic.consumer.controller;

import com.vic.commserver.feign.producer.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2021/1/5 14:06
 */
@RestController
@RequestMapping("ribbon")
public class RibbonController {

    @Autowired
    ProducerClient producerClient;

    @GetMapping("t1")
    public String t1() {
        return producerClient.ribbonTest1();
    }


}
