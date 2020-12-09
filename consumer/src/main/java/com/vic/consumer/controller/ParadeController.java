package com.vic.consumer.controller;

import com.vic.commserver.feign.producer.ProducerClient1;
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
@RequestMapping("parade")
public class ParadeController {

    @Autowired
    ProducerClient1 producerClient1;

    @GetMapping("getCause")
    public String getCause(@RequestParam("name") String name) {
        return producerClient1.getCause(name);
    }


}
