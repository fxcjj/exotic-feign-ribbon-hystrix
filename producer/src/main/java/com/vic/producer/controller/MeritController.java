package com.vic.producer.controller;

import com.alibaba.fastjson.JSON;
import com.vic.commcommon.pojo.BoilerplateParam;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 罗利华
 * date: 2020/11/30 19:26
 */
@RestController
@RequestMapping("merit")
public class MeritController {

    @GetMapping("bracket")
    public String bracket() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "bracket";
     }

    /**
     * @SpringQueryMap 可以
     * @param param
     * @return
     */
    @GetMapping("boilerplate")
    public String boilerplate(@SpringQueryMap BoilerplateParam param) {
        return "boilerplate:" + JSON.toJSONString(param);
     }

    /**
     * 获取throwable
     * @return
     */
    @GetMapping("getCause")
    public String getCause(@RequestParam(value = "name", required = false) String name) {
        if(!StringUtils.hasText(name)) {
            throw new RuntimeException("name参数为空");
        }
        return "hello, " + name;
    }

}
