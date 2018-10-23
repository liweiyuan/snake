package com.learn.snake.plugin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

/**
 * @Author :lwy
 * @Date : 2018/10/23 18:06
 * @Description : 类与方法匹配规则
 */
public interface InterceptPoint {

    /**
     * 类匹配规则
     */
    ElementMatcher<TypeDescription> buildTypeMatcher();
    /**
     * 方法匹配规则
     */
    ElementMatcher<MethodDescription> buildMethodMatcher();

}
