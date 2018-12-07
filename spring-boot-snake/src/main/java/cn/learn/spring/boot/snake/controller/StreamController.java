package cn.learn.spring.boot.snake.controller;

import cn.learn.spring.boot.snake.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author :lwy
 * @Date : 2018/10/23 16:41
 * @Description :
 */
@RestController
public class StreamController {


    @Autowired
    private StreamService streamService;

    @RequestMapping("/stream")
    public void stream(@RequestBody String body) {
        System.err.println(body);
    }

    @GetMapping("/hello")
    public String hello() {
        return streamService.sayHello("sayHello");
    }
}
