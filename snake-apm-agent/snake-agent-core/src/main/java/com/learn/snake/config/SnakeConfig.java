package com.learn.snake.config;

import com.learn.snake.constant.SystemKey;
import com.learn.snake.util.SnakeUtils;

/**
 * @Author :lwy
 * @Date : 2018/10/23 11:00
 * @Description :
 */
public class SnakeConfig extends AbstractSnakeConfig {

    private static SnakeConfig config = null;

    private Boolean isLogConsole;
    private int ratio;
    private String server;
    private String cluster;
    private String ip;
    private String port;

    //初始化参数
    public static SnakeConfig init() {

        if (config == null) {
            synchronized (SnakeConfig.config) {
                if (config == null) {
                    config = new SnakeConfig();
                    //注册config到系统资源
                    SnakeConfigFactory.init().registerConfig("snake", config);
                }
            }
        }
        return config;
    }

    public SnakeConfig() {
        initConfig();
    }

    @Override
    public void initConfig() {

        //后续添加日志级别的判断
        isLogConsole = ConfigUtils.init().getBoolean("logger.console", false);

        ratio = ConfigUtils.init().getInt("collect.ratio", 10000);
        server = System.getProperty(SystemKey.SNAKE_SERVER, "unknown");
        cluster = System.getProperty(SystemKey.SNAKE_CLUSTER, "unknown");
        port = System.getProperty(SystemKey.SNAKE_PORT, "-9999");
        ip = System.getProperty(SystemKey.SNAKE_IP);
        if (ip == null) {
            ip = SnakeUtils.getLocalIp();
        }
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getCluster() {
        return cluster;
    }

    public String getServer() {
        return server;
    }


}
