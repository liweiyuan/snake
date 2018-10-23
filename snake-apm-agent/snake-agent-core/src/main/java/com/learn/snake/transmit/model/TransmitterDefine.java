package com.learn.snake.transmit.model;

/**
 * @Author :lwy
 * @Date : 2018/10/23 15:01
 * @Description :
 */
public class TransmitterDefine {

    private String name;
    private String clazz;

    public TransmitterDefine(String name, String clazz) {
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
