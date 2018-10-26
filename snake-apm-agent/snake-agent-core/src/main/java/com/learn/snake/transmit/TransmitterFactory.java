package com.learn.snake.transmit;

import com.learn.snake.config.ConfigUtils;
import com.learn.snake.model.Span;
import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author :lwy
 * @Date : 2018/10/23 10:39
 * @Description : 数据传输方式初始化
 */
public class TransmitterFactory {

    private static final Logger logger = LoggerBuilder.getLogger(TransmitterFactory.class);

    private static Map<String, AbstractTransmitter> transmitterMap;

    //当前配置的传输类型
    private static AbstractTransmitter transmitter;

    private static String transmitterName;


    //队列
    private static BlockingQueue<Span> queue;

    private static final String THREAD_NAME_PREFIX = "snake-transmitter-";

    private static int idleSleep = ConfigUtils.init().getInt("transmitter.idleSleep", 100);

    private static int batchSize = ConfigUtils.init().getInt("transmitter.batchSize", 100);

    /**
     * 初始化系统配置的数据传输方式
     */
    public static void init() {
        logger.info("初始化传输方式");
        if (transmitterMap == null) {
            //传输方式名称
            transmitterName = ConfigUtils.init().getStr("transmitter.name");
            //初始化系统的传输方式
            transmitterMap = TransmitterLoader.loadTransmitters();
            transmitter = transmitterMap.get(transmitterName);
            //初始化
            transmitter.init();

            //初始化队列
            initQueue();
            //开启队列消费
            initQueueTask();
        }
    }

    /**
     * 初始化队列
     */
    private static void initQueue() {
        int queueSize = ConfigUtils.init().getInt("transmitter.queueSize", 1000);
        queue = new LinkedBlockingQueue<Span>(queueSize);
    }

    /**
     * 初始化队列消费
     */
    private static void initQueueTask() {
        logger.info("初始化消费队列。");
        int threadNum = ConfigUtils.init().getInt("transmitter.threadNum", Runtime.getRuntime().availableProcessors());
        //定时扫描任务
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadNum);
        scheduledExecutorService.submit((Runnable) () -> {
            Thread.currentThread().setName(THREAD_NAME_PREFIX + transmitterName);
            while (true) {
                doTransmit();
            }
        });
    }

    /**
     * 执行传输任务。
     */
    private static void doTransmit() {
        try {
            if (queue.isEmpty()) {
                Thread.sleep(idleSleep);
                return;
            }
            //注意有bug
            List<Span> spanList = new ArrayList<>(batchSize);
            queue.drainTo(spanList, batchSize);
            transmitter.transmit(spanList);
        } catch (InterruptedException e) {
            logger.error("传输线程被中断", e);
        }
    }

    /**
     * 往队列中添加数据
     * 如果blockingQueue可以容纳该元素，返回true,反之返回false
     */
    public static boolean offerQueue(Span span) {
        return queue.offer(span);
    }
}
