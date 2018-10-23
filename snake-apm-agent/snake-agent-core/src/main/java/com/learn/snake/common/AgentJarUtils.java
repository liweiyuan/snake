package com.learn.snake.common;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * @Author :lwy
 * @Date : 2018/10/22 12:36
 * @Description : 扫描jar工具类
 */
public class AgentJarUtils {

    private static String agentJarDirPath;
    private static String agentJarPath;

    //获取扫描的jar包路径
    public static String getAgentJarDirPath() {
        try {
            if (agentJarDirPath == null) {
                File jarFile = new File(Objects.requireNonNull(getAgentJarPath()));
                if (jarFile.exists()) {
                    agentJarDirPath = jarFile.getParentFile().getCanonicalPath();
                }
            }
            return agentJarDirPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAgentJarPath() {

        try {
            if (agentJarPath == null) {
                agentJarPath = findAgentPath();
            }
            return agentJarPath;
        } catch (IOException e) {
            //TODO  后续添加日志
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取扫描的path
     *
     * @return
     */
    private static String findAgentPath() throws IOException {

        String classPath = AgentJarUtils.class.getName().replaceAll("\\.", "/") + ".class";
        URL resource = Thread.currentThread().getContextClassLoader().getResource(classPath);
        if (resource != null) {
            String path = resource.toString();
            int jarIndex = path.indexOf('!');
            if (jarIndex > 0) {
                path = path.substring(path.indexOf("file:") + 5, jarIndex);
            } else {
                return null;
            }
            File f = new File(path);
            if (f.exists()) {
                path = f.getCanonicalPath();
            }
            System.out.println("agent jar path = " + path);
            return path;
        }

        return null;
    }

    //获取路径 jar
    public static String findAgentJarDirPath() {
        if (agentJarDirPath == null) {
            getAgentJarDirPath();
        }
        return agentJarDirPath;
    }

    public static String findAgentJarPath() {
        if (agentJarPath == null) {
            getAgentJarPath();
        }
        return agentJarPath;
    }

}
