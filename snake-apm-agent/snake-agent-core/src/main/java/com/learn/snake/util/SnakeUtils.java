package com.learn.snake.util;

import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @Author :lwy
 * @Date : 2018/10/22 18:41
 * @Description :
 */
public class SnakeUtils {

    private static final Logger logger = LoggerBuilder.getLogger(SnakeUtils.class);

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            logger.error("close the io is failed. ", e);
        }
    }

    public static String getLocalIp(){
        try{
            return InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            logger.error("获取ip失败. ", e);
        }
        return null;
    }
}
