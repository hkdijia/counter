package com.gotkx.counter.controller;

import com.gotkx.counter.util.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

    @Autowired
    private StringRedisTemplate template;

    @RequestMapping("/hello2")
    public String hell2(){
        template.opsForValue().set("test:Hello","World");
        return template.opsForValue().get("test:Hello");
    }

    @RequestMapping("/hello")
    public String hello(){
        //return "hello world";
        return "" + DbUtil.getId();
    }

}
