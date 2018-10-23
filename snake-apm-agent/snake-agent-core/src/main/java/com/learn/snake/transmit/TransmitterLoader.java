package com.learn.snake.transmit;

import com.learn.snake.common.classloader.AgentClassLoader;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.transmit.model.TransmitterDefine;
import com.learn.snake.util.LoggerBuilder;
import com.learn.snake.util.SnakeUtils;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 * @Author :lwy
 * @Date : 2018/10/23 11:40
 * @Description :传输方式初始化
 */
class TransmitterLoader {


    private static final Logger logger = LoggerBuilder.getLogger(TransmitterLoader.class);

    /**
     * 初始化系统配置的传输方式
     *
     * @return
     */
    static Map<String, AbstractTransmitter> loadTransmitters() {
        Map<String, AbstractTransmitter> transmitterList = new HashMap<String, AbstractTransmitter>();

        //定义存储传输的list
        List<TransmitterDefine> transmitterDefines = new ArrayList<TransmitterDefine>();

        AgentClassLoader classLoader = new AgentClassLoader(TransmitterLoader.class.getClassLoader(), new String[]{SystemKey.TRANSMIT_FOLDER});

        //获取资源路径
        List<URL> resources = getResources(classLoader);

        if (resources == null || resources.isEmpty()) {
            return transmitterList;
        }
        for (URL url : resources) {
            try {
                readDefinePlugin(url.openStream(), transmitterDefines);
            } catch (IOException e) {
                logger.error("io读写失败.", e);
            }
        }

        transmitterDefines.forEach(define -> {
            try {
                AbstractTransmitter transmitter = (AbstractTransmitter) Class.forName(define.getClazz(), true, classLoader).newInstance();
                transmitterList.put(define.getName(), transmitter);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("初始化传输类失败。", e);
            }
        });
        return transmitterList;
    }


    /**
     * 获取传输方式资源
     *
     * @param classLoader
     * @return
     */
    private static List<URL> getResources(AgentClassLoader classLoader) {

        List<URL> transmitcfgUrl = new ArrayList<URL>();
        Enumeration<URL> resources = null;
        try {
            resources = classLoader.getResources(SystemKey.TRANSMIT_DEF);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                transmitcfgUrl.add(url);
            }
            return transmitcfgUrl;
        } catch (IOException e) {
            logger.error("获取传输资源失败.", e);
        }
        return null;
    }

    /**
     * 获取传输的plugin
     *
     * @param inputStream
     * @param transmitterDefines
     */
    private static void readDefinePlugin(InputStream inputStream, List<TransmitterDefine> transmitterDefines) throws IOException {

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
                transmitterDefines.add(new TransmitterDefine(split[0], split[1]));
            }
        } finally {
            SnakeUtils.close(inputStream);
        }
    }
}
