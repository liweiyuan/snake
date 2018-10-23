package com.learn.snake.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author :lwy
 * @Date : 2018/10/23 11:07
 * @Description :
 */
public class SnakeConfigFactory {

    private static Map<String, AbstractSnakeConfig> configMap = new ConcurrentHashMap<String, AbstractSnakeConfig>();

    private static SnakeConfigFactory instance;

    public static SnakeConfigFactory init(){
        if(instance==null){
            synchronized (SnakeConfigFactory.class){
                if(instance==null){
                    instance=new SnakeConfigFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 注册系统config
     */
    public void registerConfig(String configKey, AbstractSnakeConfig config) {
        configMap.put(configKey, config);
    }
}
