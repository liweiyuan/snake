package com.learn.snake.transmit;

import com.learn.snake.model.Span;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author :lwy
 * @Date : 2018/10/23 10:48
 * @Description :
 */
public abstract class AbstractTransmitter {

    public static BlockingQueue<List<Span>> storeQueue=new LinkedBlockingQueue<>(100);

    public String name;

    public abstract int transmit(Span span);

    public abstract int transmit(List<Span> list);

    public abstract int init();
}
