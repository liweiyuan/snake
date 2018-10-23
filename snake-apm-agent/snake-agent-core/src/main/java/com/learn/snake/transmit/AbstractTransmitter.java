package com.learn.snake.transmit;

import com.learn.snake.model.Span;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/23 10:48
 * @Description :
 */
public abstract class AbstractTransmitter {

    public String name;

    public abstract int transmit(Span span);

    public abstract int transmit(List<Span> list);

    public abstract int init();
}
