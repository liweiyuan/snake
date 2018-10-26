package com.learn.snake.plugin.handler;

import com.learn.snake.common.classloader.AgentClassLoader;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.util.LoggerBuilder;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @Author :lwy
 * @Date : 2018/10/24 16:20
 * @Description : handler加载器
 */
public class HandlerLoader {

    private static final Logger logger = LoggerBuilder.getLogger(HandlerLoader.class);

    //handler存储器
    private static Map<String, IHandler> handlerMap = new ConcurrentHashMap<String, IHandler>();

    //对应Class存储器
    private static Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    private static AgentClassLoader snakeClassLoader;

    //handler重入锁
    private static Lock HANDLER_LOCK = new ReentrantLock();

    private static AgentClassLoader getSnakeClassLoader(ClassLoader parentClassLoader) {
        if (snakeClassLoader == null) {
            synchronized (HandlerLoader.class) {
                if (snakeClassLoader == null) {
                    snakeClassLoader = new AgentClassLoader(parentClassLoader, new String[]{SystemKey.PLUGIN_FOLDER});
                }
            }
        }
        return snakeClassLoader;
    }

    /**
     * 获取对应的handler处理器
     *
     * @param className
     * @return
     */
    public static IHandler loadHandler(String className) {
        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = getSnakeClassLoader(parentClassLoader);
        String handlerKey = className + "_OF_" + classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());

        IHandler iHandler = handlerMap.get(handlerKey);
        if (iHandler == null) {
            HANDLER_LOCK.lock();
            try {
                iHandler = (IHandler) Class.forName(className, true, classLoader).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("创建一个handler失败", e);
                //抛出异常则返回默认的空handler处理逻辑
                return new EmptyHandler();
            } finally {
                HANDLER_LOCK.unlock();
            }
            if (iHandler != null) {
                handlerMap.put(handlerKey, iHandler);
            }//修复默认的handler相关bug
            return iHandler;

        } else {
            return iHandler;
        }

    }

    /**
     * 创建一个class
     *
     * @param className
     * @return
     */
    public static Class<?> loadClass(String className) {
        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = getSnakeClassLoader(parentClassLoader);
        String handlerKey = className + "_OF_" + classLoader.getClass().getName() + "@" + Integer.toHexString(classLoader.hashCode());
        Class<?> clzz = classMap.get(handlerKey);
        if (clzz == null) {
            HANDLER_LOCK.lock();
            try {
                clzz = (Class<?>) Class.forName(className, true, classLoader).newInstance();
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                logger.error("创建一个Class失败", e);
            } finally {
                HANDLER_LOCK.unlock();
            }
            if (clzz != null) {
                classMap.put(handlerKey, clzz);
                return clzz;
            }
        }
        return null;
    }


}
