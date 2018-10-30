package com.learn.snake.servlet.plugin;

import com.learn.snake.plugin.AbstractPlugin;
import com.learn.snake.plugin.InterceptPoint;
import com.learn.snake.servlet.plugin.interceptor.ServletAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Author :lwy
 * @Date : 2018/10/30 14:28
 * @Description :
 */
public class ServletPlugin extends AbstractPlugin {
    @Override
    public String getName() {
        return "servlet";
    }

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        return new InterceptPoint[]{new ServletInterceptPoint()};
    }

    @Override
    public Class interceptorAdviceClass() {
        return ServletAdvice.class;
    }

    private class ServletInterceptPoint implements InterceptPoint {

        private static final String CLASS_TYPE_SERVLET_NAME = "javax.servlet.http.HttpServlet";
        private static final String CLASS_TYPE_SERVLET_REQUEST_NAME = "javax.servlet.http.HttpServletRequest";
        private static final String CLASS_TYPE_SERVLET_RESPONSE_NAME = "javax.servlet.http.HttpServletResponse";

        //类匹配
        @Override
        public ElementMatcher<TypeDescription> buildTypesMatcher() {

            //HttpServlet及其子类，并且不是抽象类
            return ElementMatchers.hasSuperType(ElementMatchers.<TypeDescription>named(CLASS_TYPE_SERVLET_NAME))
                    .and(ElementMatchers.not(ElementMatchers.isAbstract()));
        }

        //方法匹配
        @Override
        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
            return ElementMatchers.isMethod()
                    .and(ElementMatchers.takesArguments(2))
                    .and(ElementMatchers.takesArgument(0, ElementMatchers.<TypeDescription>named(CLASS_TYPE_SERVLET_REQUEST_NAME)))
                    .and(ElementMatchers.takesArgument(1, ElementMatchers.<TypeDescription>named(CLASS_TYPE_SERVLET_RESPONSE_NAME)))
                    .and(ElementMatchers.<MethodDescription>nameStartsWith("do"));
        }
    }
}
