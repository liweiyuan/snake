package com.learn.snake.model;

import java.util.HashMap;

/**
 * @Author :lwy
 * @Date : 2018/10/23 10:51
 * @Description :
 */
public class Tags extends HashMap<String, Object> {

    public Tags addTags(String key, Object value) {
        this.put(key, value);
        return this;
    }

}
