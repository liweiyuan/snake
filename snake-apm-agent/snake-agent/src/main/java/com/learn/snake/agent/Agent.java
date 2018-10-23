package com.learn.snake.agent;

import com.learn.snake.common.AgentJarUtils;
import com.learn.snake.common.IdHelper;
import com.learn.snake.plugin.AbstractPlugin;
import com.learn.snake.plugin.PluginLoader;
import com.learn.snake.transmit.TransmitterFactory;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatchers;

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
        List<AbstractPlugin> plugins=PluginLoader.loadPlugins();

        //5.初始化Agent
        AgentBuilder agentBuilder = new AgentBuilder.Default().ignore(ElementMatchers.<TypeDescription>nameStartsWith("com.learn.snake"));
    }

}
