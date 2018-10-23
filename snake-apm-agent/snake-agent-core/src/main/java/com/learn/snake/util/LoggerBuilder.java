package com.learn.snake.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author :lwy
 * @Date : 2018/10/22 18:42
 * @Description :
 */
public class LoggerBuilder {

    public static Logger getLogger(Class<?> clzz) {
        return LoggerFactory.getLogger(clzz);
    }
}
