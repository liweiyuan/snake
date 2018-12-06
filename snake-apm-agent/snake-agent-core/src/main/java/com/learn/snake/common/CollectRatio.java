package com.learn.snake.common;

import com.learn.snake.config.SnakeConfig;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author :lwy
 * @Date : 2018/10/26 10:47
 * @Description :
 */
public class CollectRatio {

    private static AtomicLong total = new AtomicLong(0);
    private static AtomicLong currNum = new AtomicLong(0);


    private static long incrTotal() {
        return total.incrementAndGet();
    }

    public static long getTotal() {
        return total.get();
    }

    private static long incrCurrNum() {
        return currNum.incrementAndGet();
    }

    private static long getCurrNum() {
        return currNum.get();
    }

    /**
     * 确定当前的数据收集比率
     *
     * @return
     */
    public static boolean yes() {
        return isCollect();
    }

    /**
     * 蔡妍率是否关闭
     * @return
     */
    public static boolean no(){
        return !yes();
    }


    private static boolean isCollect() {

        if (SnakeConfig.init().getRatio() <= 0) {
            SnakeTraceContext.setCTag("N");
            return false;
        } else if (SnakeConfig.init().getRatio() >= 1000) {
            SnakeTraceContext.setCTag("Y");
            return true;
        }
        //采样率的判断
        String cTag = SnakeTraceContext.getCTag();
        if ("Y".equals(cTag)) {
            return true;
        } else if ("N".equals(cTag)) {
            return false;
        } else if (incrCurrNum() == 0) { //第一条数据
            incrCurrNum();
            incrTotal();
            SnakeTraceContext.setCTag("Y");
            return true;
        }
        long tmpTotal = incrTotal();
        long tmpCurrNum = getCurrNum() + 1;
        Double rate = tmpCurrNum * 1.0 / tmpTotal * 10000;
        if (rate.intValue() > SnakeConfig.init().getRatio()) {
            SnakeTraceContext.setCTag("N");
            return false;
        }
        incrCurrNum();
        SnakeTraceContext.setCTag("Y");
        return true;

    }
}
