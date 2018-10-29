package com.learn.snake.agent.transmitter.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.learn.snake.model.Span;
import com.learn.snake.transmit.AbstractTransmitter;
import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/23 16:30
 * @Description :
 */
public class OkHttpTransmitter extends AbstractTransmitter implements Runnable {

    private static final Logger logger = LoggerBuilder.getLogger(OkHttpTransmitter.class);

    public String name = "okhttp";

    @Override
    public int transmit(Span span) {
        OkHttpHelper.getInstance().post(JSON.toJSONString(span));
        return 1;
    }

    @Override
    public int transmit(List<Span> list) {
        //添加到队列中
        try {
            storeQueue.put(list);
        } catch (InterruptedException e) {
            logger.error("放入到队列失败。", e);
        }

        //初始化线程
        Thread thread = new Thread(new OkHttpTransmitter());
        thread.start();
        return list.size();
    }

    @Override
    public int init() {
        return 0;
    }


    /**
     * 消费队列中的数据
     */
    @Override
    public void run() {
        List<Span> spanList = null;
        while (true) {
            try {
                spanList = storeQueue.take();
            } catch (InterruptedException e) {
                logger.error("从队列中获取元素失败", e);
            }
            //调用传输
            OkHttpHelper.getInstance().post(JSON.toJSONString(spanList,SerializerFeature.IgnoreErrorGetter));
            //清空
            assert spanList != null;
            spanList.clear();
        }
    }
}
