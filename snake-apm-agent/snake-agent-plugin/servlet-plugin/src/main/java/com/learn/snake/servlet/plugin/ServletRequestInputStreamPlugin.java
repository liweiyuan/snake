package com.learn.snake.servlet.plugin;

import com.learn.snake.plugin.AbstractPlugin;
import com.learn.snake.plugin.InterceptPoint;
import com.learn.snake.servlet.plugin.interceptor.ServletRequestInputStreamAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Author :lwy
 * @Date : 2018/12/7 10:38
 * @Description : http body参数采集嵌码入口
 */
public class ServletRequestInputStreamPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "requestInputStream";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{new InnerServletRequestInputStreamInterceptPoint()};
    }

    @Override
    public Class interceptorAdviceClass() {
        return ServletRequestInputStreamAdvice.class;
    }

    private class InnerServletRequestInputStreamInterceptPoint implements InterceptPoint {

        private static final String CLASS_TYPE_SERVLET_REQUEST_NAME = "javax.servlet.http.HttpServletRequest";

        private static final String METHOD_NAME = "getInputStream";

        @Override
        public ElementMatcher<TypeDescription> buildTypesMatcher() {
            return ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(CLASS_TYPE_SERVLET_REQUEST_NAME)
                    .and(ElementMatchers.not(ElementMatchers.isAbstract()))
                    .and(ElementMatchers.not(ElementMatchers.isInterface())));
        }

        @Override
        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
            return ElementMatchers.<MethodDescription>named(METHOD_NAME);
        }
    }
}
