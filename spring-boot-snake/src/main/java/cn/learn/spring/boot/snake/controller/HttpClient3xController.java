package cn.learn.spring.boot.snake.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Author :lwy
 * @Date : 2018/10/29 15:28
 * @Description :
 */
@RestController
public class HttpClient3xController {

    private static String url = "http://localhost:7005/execute";

    @GetMapping("/http3x")
    public String request() {


        HttpClient httpClient = new HttpClient();
        HttpMethod httpMethod = new GetMethod(url);
        //发送请求
        int statusCode = 0;
        try {
            statusCode = httpClient.executeMethod(httpMethod);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (statusCode == 200) {
            return "success";
        } else {
            return "fail";
        }
    }

    @GetMapping("/execute")
    public void execute() {
        System.err.println("execute");
    }

    @PostMapping("/execute")
    public void execute2() {
        System.err.println("execute2");
    }

}
