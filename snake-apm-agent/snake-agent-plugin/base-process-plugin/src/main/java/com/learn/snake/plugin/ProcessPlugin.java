package com.learn.snake.plugin;

import com.learn.snake.common.MatchKit;
import com.learn.snake.config.ConfigUtils;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.plugin.interceptor.ProcessAdvice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/24 14:43
 * @Description :  主程序拦截类型
 */
public class ProcessPlugin extends AbstractPlugin {

    @Override
    public String getName() {
        return "process";
    }

    public InterceptPoint[] buildInterceptPoint() {
        List<Map<String, Map<String, Map<String, String>>>> list =
                (List<Map<String, Map<String, Map<String, String>>>>) ConfigUtils.init().getVal(SystemKey.PLUGIN_INTERCEPTOR_POINTS);
        if (list == null || list.isEmpty()) {
            DefaultInterceptPoint defaultInterceptPoint = new DefaultInterceptPoint();
            return new InterceptPoint[]{defaultInterceptPoint};
        }
        InterceptPoint[] points = new InterceptPoint[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Map<String, Map<String, String>>> item = list.get(i);
            if (item == null || item.isEmpty()) {
                continue;
            }
            final Map<String, Map<String, String>> typeMatch = item.get("typeMatch");
            final Map<String, Map<String, String>> methodMatch = item.get("methodMatch");
            //构造InterceptorPoint数组
            points[i] = new CommonInterceptorPoint(typeMatch, methodMatch);
        }
        return points;
    }

    @Override
    public Class interceptorAdviceClass() {
        return ProcessAdvice.class;
    }


    private class DefaultInterceptPoint implements InterceptPoint {


        @Override
        public ElementMatcher<TypeDescription> buildTypesMatcher() {
            return MatchKit.buildTypesMatcher(null, null)
                    .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
        }

        @Override
        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
            return MatchKit.buildMethodsMatcher(null, null);
        }
    }


    private class CommonInterceptorPoint implements InterceptPoint {

        private Map<String, Map<String, String>> typeMatch;
        private Map<String, Map<String, String>> methodMatch;

        CommonInterceptorPoint(Map<String, Map<String, String>> typeMatch, Map<String, Map<String, String>> methodMatch) {
            this.typeMatch = typeMatch;
            this.methodMatch = methodMatch;
        }

        @Override
        public ElementMatcher<TypeDescription> buildTypesMatcher() {
            if (typeMatch == null) {
                return MatchKit.buildTypesMatcher(null, null)
                        .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
            }
            return MatchKit.buildTypesMatcher(typeMatch.get("include"), typeMatch.get("exclude"))
                    .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
        }

        @Override
        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
            if (methodMatch == null) {
                return MatchKit.buildMethodsMatcher(null, null);
            }
            return MatchKit.buildMethodsMatcher(methodMatch.get("include"), methodMatch.get("exclude"));
        }
    }
}

