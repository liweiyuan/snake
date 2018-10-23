package com.learn.snake.plugin;

import com.learn.snake.common.classloader.AgentClassLoader;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.plugin.model.PluginDefine;
import com.learn.snake.transmit.model.TransmitterDefine;
import com.learn.snake.util.LoggerBuilder;
import com.learn.snake.util.SnakeUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/23 18:17
 * @Description :
 */
public class PluginLoader {

    private static final Logger logger = LoggerBuilder.getLogger(PluginLoader.class);

    /**
     * 加载plugins
     *
     * @return
     */
    public static List<AbstractPlugin> loadPlugins() {
        List<AbstractPlugin> plugins = new ArrayList<>(16);
        List<PluginDefine> pluginDefines = new ArrayList<>(16);

        AgentClassLoader classLoader = new AgentClassLoader(PluginLoader.class.getClassLoader(), SystemKey.PLUGINS);
        List<URL> resources = getResources(classLoader);
        if (resources == null || resources.isEmpty()) {
            return plugins;
        }
        resources.forEach(url -> {
            try {
                readDefinePlugin(url.openStream(), pluginDefines);
            } catch (IOException e) {
                logger.error("io读写失败.", e);
            }
        });

        //遍历生成plugin
        pluginDefines.forEach(pluginDefine -> {
            try {
                AbstractPlugin plugin = (AbstractPlugin) Class.forName(pluginDefine.getClazz(), true, classLoader).newInstance();
                plugins.add(plugin);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("初始化plugin失败。", e);
            }
        });
        return plugins;
    }


    /**
     * 获取资源
     *
     * @param classLoader
     * @return
     */
    private static List<URL> getResources(AgentClassLoader classLoader) {
        List<URL> urlList = new ArrayList<>(16);
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources(SystemKey.PLUGIN_DEF);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                urlList.add(url);
            }
            return urlList;
        } catch (IOException e) {
            logger.error("获取plugins时ioException", e);
        }
        return null;
    }

    private static void readDefinePlugin(InputStream inputStream, List<PluginDefine> pluginDefines) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String define;
            while ((define = reader.readLine()) != null) {
                if (define.trim().length() == 0 || define.startsWith("#")) {
                    continue;
                }
                define = define.trim();
                String[] split = define.split("=");
                if (split.length != 2) {
                    continue;
                }
                pluginDefines.add(new PluginDefine(split[0], split[1]));
            }
        } finally {
            SnakeUtils.close(inputStream);
        }
    }
}
