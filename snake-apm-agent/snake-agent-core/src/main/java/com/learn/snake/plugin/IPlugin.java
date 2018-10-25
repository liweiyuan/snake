package com.learn.snake.plugin;

/**
 * @Author :lwy
 * @Date : 2018/10/23 18:04
 * @Description : plugin抽象接口
 */
public interface IPlugin {

    String getName();

    /**
     * 类与方法匹配规则
     *
     * @return
     */
    InterceptPoint[] buildInterceptPoint();

    /**
     * 拦截器类名称
     *
     * @return
     */
    Class interceptorAdviceClass();
}
