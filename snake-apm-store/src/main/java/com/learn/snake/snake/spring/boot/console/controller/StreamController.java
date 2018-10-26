package com.learn.snake.snake.spring.boot.console.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author :lwy
 * @Date : 2018/10/26 15:35
 * @Description :
 */
@Controller
public class StreamController {


    @RequestMapping("/stream")
    @ResponseBody
    public String stream(@RequestBody String body) {
        System.out.println(body);
        return "";
    }
}
