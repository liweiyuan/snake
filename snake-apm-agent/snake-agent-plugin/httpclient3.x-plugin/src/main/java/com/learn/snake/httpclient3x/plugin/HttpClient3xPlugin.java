package com.learn.snake.httpclient3x.plugin;

import com.learn.snake.httpclient3x.plugin.interceptor.HttpClient3xAdvice;
import com.learn.snake.plugin.AbstractPlugin;
import com.learn.snake.plugin.InterceptPoint;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Author :lwy
 * @Date : 2018/10/29 15:24
 * @Description :
 */
public class HttpClient3xPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "httpclient3x";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{new HttpClient3xInterceptorPoint()};
    }

    @Override
    public Class interceptorAdviceClass() {
        return HttpClient3xAdvice.class;
    }

    private class HttpClient3xInterceptorPoint implements InterceptPoint {

        private static final String HTTP_CLIENT_MATCHER = "org.apache.commons.httpclient.HttpClient";

        @Override
        public ElementMatcher<TypeDescription> buildTypesMatcher() {

            //主要关注的类是org.apache.commons.httpclient.HttpClient及其子类
            return ElementMatchers.hasSuperType(ElementMatchers.named(HTTP_CLIENT_MATCHER));
        }

        @Override
        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
            //主要关注的方法是executeMethod
            return ElementMatchers.isMethod().and(ElementMatchers.<MethodDescription>named("executeMethod"));
        }
    }
}

