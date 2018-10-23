package com.learn.snake.common.classloader;

import com.learn.snake.common.AgentJarUtils;
import com.learn.snake.util.LoggerBuilder;
import com.learn.snake.util.SnakeUtils;
import org.slf4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author :lwy
 * @Date : 2018/10/23 15:04
 * @Description :
 */
public class AgentClassLoader extends ClassLoader {

    private static final Logger logger = LoggerBuilder.getLogger(AgentClassLoader.class);

    private List<File> jarPathDir;
    private List<File> jarFiles;

    public AgentClassLoader(ClassLoader parent, String[] jarFolder) {
        super(parent);
        jarPathDir = new ArrayList<File>();
        for (String aJarFolder : jarFolder) {
            jarPathDir.add(new File(AgentJarUtils.getAgentJarDirPath() + "/" + aJarFolder));
        }
        jarFiles = getJarFiles();
    }

    /**
     * 获取传输jar文件
     *
     * @return
     */
    private List<File> getJarFiles() {
        jarFiles = new ArrayList<>(16);
        for (File dir : jarPathDir) {
            if (dir.exists() && dir.isDirectory()) {
                String[] jarFileNames = dir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".jar");
                    }
                });

                if (jarFileNames == null) {
                    return jarFiles;
                }
                for (String fileName : jarFileNames) {
                    if (fileName != null) {
                        File file = new File(dir, fileName);
                        if (file.exists()) {
                            jarFiles.add(file);
                        }
                    }
                }
            }
        }
        return jarFiles;
    }

    /**
     * 读取class文件
     *
     * @param jarPath
     * @param className
     * @return
     */
    private byte[] readClassFile(String jarPath, String className) throws IOException {
        className = className.replace('.', '/').concat(".class");
        URL classFileUrl = new URL("jar:file:" + jarPath + "!/" + className);
        byte[] data = null;
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(classFileUrl.openStream());
            byteArrayOutputStream = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = bufferedInputStream.read()) != -1) {
                byteArrayOutputStream.write(ch);
            }
            data = byteArrayOutputStream.toByteArray();
        } finally {
            //关闭流
            if (bufferedInputStream != null) {
                SnakeUtils.close(bufferedInputStream);
            }
            if (byteArrayOutputStream != null) {
                SnakeUtils.close(byteArrayOutputStream);
            }
        }
        return data;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = name.replace('.', '/').concat(".class");
        for (File file : jarFiles) {
            try {
                JarFile jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(path);
                if (entry != null) {
                    byte[] data = readClassFile(file.getAbsolutePath(), name);
                    return defineClass(name, data, 0, data.length);
                }
            } catch (Exception e) {
                logger.error("ioException", e);
            }
        }
        throw new ClassNotFoundException("Can't find " + name);
    }


    @Override
    protected URL findResource(String name) {
        try {
            for (File file : jarFiles) {
                JarFile jar = new JarFile(file);
                JarEntry entry = jar.getJarEntry(name);
                if (entry != null) {
                    try {
                        return new URL("jar:file:" + file.getAbsolutePath() + "!/" + name);
                    } catch (MalformedURLException e) {
                        logger.error("MalformedURLException", e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Enumeration<URL> findResources(String name) {

        List<URL> allResources = new ArrayList<>();
        jarFiles.forEach(
                jarFile -> {
                    try {
                        JarFile jar = new JarFile(jarFile);
                        JarEntry jarEntry = jar.getJarEntry(name);
                        if (jarEntry != null) {
                            allResources.add(new URL("jar:file:" + jarFile.getAbsolutePath() + "!/" + name));
                        }
                    } catch (IOException e) {
                        logger.error("ioException", e);
                    }
                }
        );
        final Iterator<URL> iterator = allResources.iterator();
        return new Enumeration<URL>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public URL nextElement() {
                return iterator.next();
            }
        };
    }
}
