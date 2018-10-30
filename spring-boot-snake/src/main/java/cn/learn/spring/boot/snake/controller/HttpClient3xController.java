package cn.learn.spring.boot.snake.controller;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

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
    public void execute(HttpServletRequest request) {
        //获取header
        Enumeration<String> headerNames = request.getHeaderNames();
        if(headerNames.hasMoreElements()){
            String headerKey = headerNames.nextElement();
            System.out.println(request.getHeader(headerKey));
        }

        //实现重大突破。通过header来传递数据
        System.err.println("execute");
    }

    @PostMapping("/execute")
    public void execute2() {
        System.err.println("execute2");
    }

}
