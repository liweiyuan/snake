package com.learn.snake.agent.transmitter.okhttp;

import com.alibaba.fastjson.JSON;
import com.learn.snake.model.Span;
import com.learn.snake.transmit.AbstractTransmitter;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/23 16:30
 * @Description :
 */
public class OkHttpTransmitter extends AbstractTransmitter {
    public String name = "okhttp";

    @Override
    public int transmit(Span span) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(span));
        return 1;
    }

    @Override
    public int transmit(List<Span> list) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(list));
        return list.size();
    }

    @Override
    public int init() {
        return 0;
    }
}
