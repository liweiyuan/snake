package com.learn.snake.plugin;

import com.learn.snake.common.MatchKit;
import com.learn.snake.config.ConfigUtils;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.plugin.interceptor.ProcessAdvice;
import net.bytebuddy.description.method.MethodDescription;
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

    @Override
    public InterceptPoint[] buildInterceptPoint() {
        Object value = ConfigUtils.init().getVal(SystemKey.PLUGIN_INTERCEPTOR_POINTS);
        List<Map<String, Map<String, Map<String, String>>>> list = (List<Map<String, Map<String, Map<String, String>>>>) value;
        if (list == null || list.isEmpty()) {
            return new InterceptPoint[]{
                    new InterceptPoint() {
                        @Override
                        public ElementMatcher.Junction buildTypesMatcher() {
                            return MatchKit.buildTypesMatcher(null, null)
                                    .and(ElementMatchers.not(ElementMatchers.hasSuperType(ElementMatchers.named("javax.servlet.http.HttpServlet"))));
                        }

                        @Override
                        public ElementMatcher<MethodDescription> buildMethodsMatcher() {
                            return MatchKit.buildMethodsMatcher(null, null);
                        }
                    }
            };
        }
        InterceptPoint[] points = new InterceptPoint[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Map<String, Map<String, Map<String, String>>> item = list.get(i);
            if (item == null || item.isEmpty()) {
                continue;
            }
            //获取配置文件中的typeMatch与methodMatch
            final Map<String, Map<String, String>> typeMatch = item.get(SystemKey.PLUGIN_TYPE_MATCH);
            final Map<String, Map<String, String>> methodMatch = item.get(SystemKey.PLUGIN_METHOD_MATCH);

            points[i] = new InterceptPoint() {
                @Override
                public ElementMatcher.Junction buildTypesMatcher() {
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
            };
        }
        return points;
    }

    @Override
    public Class interceptorAdviceClass() {
       return ProcessAdvice.class;
    }
}