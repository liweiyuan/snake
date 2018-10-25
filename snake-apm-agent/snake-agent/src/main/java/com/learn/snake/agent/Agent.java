package com.learn.snake.agent;

import com.learn.snake.common.AgentJarUtils;
import com.learn.snake.common.IdHelper;
import com.learn.snake.model.FieldDefine;
import com.learn.snake.plugin.AbstractPlugin;
import com.learn.snake.plugin.InterceptPoint;
import com.learn.snake.plugin.PluginLoader;
import com.learn.snake.transmit.TransmitterFactory;
import com.learn.snake.util.LoggerBuilder;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;

import java.lang.instrument.Instrumentation;
import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/22 11:21
 * @Description :
 */
public class Agent {

    public static void premain(String arguments, Instrumentation instrumentation) {


        //1.初始化扫描路径
        AgentJarUtils.getAgentJarDirPath();

        //2.初始化系统参数
        IdHelper.init();
        //3.初始化传输方式
        TransmitterFactory.init();

        //4.扫描需要适配的plugin
        List<AbstractPlugin> plugins = PluginLoader.loadPlugins();

        //5.初始化Agent
        AgentBuilder agentBuilder = new AgentBuilder.Default().ignore(ElementMatchers.nameStartsWith("com.learn.snake."));


        for (int i = 0; i < plugins.size(); i++) {
            final AbstractPlugin plugin = plugins.get(i);

            InterceptPoint[] points = plugin.buildInterceptPoint();
            for (int j = 0; j < points.length; j++) {
                final InterceptPoint interceptPoint = points[j];
                AgentBuilder.Transformer transformer = new AgentBuilder.Transformer() {

                    private final Logger logger = LoggerBuilder.getLogger(AgentBuilder.Transformer.class);

                    @Override
                    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                                            TypeDescription typeDescription,
                                                            ClassLoader classLoader,
                                                            JavaModule module) {
                        String className = typeDescription.getCanonicalName();
                        logger.info("class name : " + className);

                        //class与方法拦截
                        builder = builder.visit(Advice.to(plugin.interceptorAdviceClass()).on(interceptPoint.buildMethodsMatcher()));

                        FieldDefine[] fieldDefines = plugin.buildFieldDefine();
                        if (fieldDefines != null && fieldDefines.length > 0) {
                            for (FieldDefine define : fieldDefines) {
                                builder = builder.defineField(define.name, define.type, define.modifiers);
                            }
                        }
                        return builder;
                    }
                };
                agentBuilder = agentBuilder.type(interceptPoint.buildTypesMatcher()).transform(transformer).asDecorator();
            }
        }


        //agent监听器
        AgentBuilder.Listener listener = new AgentBuilder.Listener() {
            private final Logger log = LoggerBuilder.getLogger(AgentBuilder.Listener.class);

            @Override
            public void onDiscovery(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }

            @Override
            public void onTransformation(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b, DynamicType dynamicType) {
                WeavingClassLog.INSTANCE.log(typeDescription, dynamicType);
            }

            @Override
            public void onIgnored(TypeDescription typeDescription, ClassLoader classLoader, JavaModule javaModule, boolean b) {
            }

            @Override
            public void onError(String s, ClassLoader classLoader, JavaModule javaModule, boolean b, Throwable throwable) {
                log.error("", throwable);
            }

            @Override
            public void onComplete(String s, ClassLoader classLoader, JavaModule javaModule, boolean b) {

            }
        };
        agentBuilder.with(listener).installOn(instrumentation);
        //agentBuilder.with(listener).with(AgentBuilder.Listener.StreamWriting.toSystemError()).installOn(instrumentation);
    }

}
