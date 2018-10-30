package com.learn.snake.agent;

import com.learn.snake.common.AgentJarUtils;
import com.learn.snake.util.LoggerBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public enum WeavingClassLog {
    INSTANCE;
    private File weavingClassLogPath;

    private static final Logger logger=LoggerBuilder.getLogger(WeavingClassLog.class);

    /**
     * 保存修改完毕的字节码文件
     * @param typeDescription
     * @param dynamicType
     */
    public void saveClassCode(TypeDescription typeDescription, DynamicType dynamicType) {
        synchronized (INSTANCE) {
            try {
                if (weavingClassLogPath == null) {
                    try {
                        weavingClassLogPath = new File(AgentJarUtils.getAgentJarDirPath()+"/weaving-class");
                        if (!weavingClassLogPath.exists()) {
                            weavingClassLogPath.mkdir();
                        }
                    } catch (Exception e) {
                        logger.error("字节码路径有误",e);
                    }
                }

                try {
                    dynamicType.saveIn(weavingClassLogPath);
                } catch (IOException e) {
                    logger.error("修改完毕的字节码保存失败",e);
                }
            } catch (Throwable t) {
                logger.error("",t);
            }
        }
    }
}
