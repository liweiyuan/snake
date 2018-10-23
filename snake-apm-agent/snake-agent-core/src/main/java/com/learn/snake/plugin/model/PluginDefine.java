package com.learn.snake.plugin.model;

/**
 * @Author :lwy
 * @Date : 2018/10/23 18:18
 * @Description :
 */
public class PluginDefine {

    private String name;

    private String clazz;

    public PluginDefine(String name, String clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }
}
