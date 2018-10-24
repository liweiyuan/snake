package com.learn.snake.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.learn.snake.common.AgentJarUtils;
import com.learn.snake.constant.SystemKey;
import com.learn.snake.util.LoggerBuilder;
import com.learn.snake.util.SnakeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @Author :lwy
 * @Date : 2018/10/22 17:51
 * @Description :
 */
public class ConfigUtils {

    private static final Logger logger = LoggerBuilder.getLogger(ConfigUtils.class);

    private static JSONObject jsonConfig = null;
    private static ConfigUtils instance = null;

    public ConfigUtils() {

        //初始化系统参数
        initAppParameter();
    }

    private void initAppParameter() {
        String path = System.getProperty(SystemKey.SNAKE_CONFIG);
        if (StringUtils.isBlank(path)) {
            path = AgentJarUtils.findAgentJarDirPath() + SystemKey.CONFIG_PATH;
        }
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
            Yaml yaml = new Yaml();
            Map loadMap = yaml.load(inputStream);
            jsonConfig = JSONObject.parseObject(JSONObject.toJSONString(loadMap));
        } catch (FileNotFoundException e) {
            logger.error("file is not found. ", e);
        } finally {
            if (inputStream != null) {
                SnakeUtils.close(inputStream);
            }
        }
    }

    /**
     * init
     *
     * @return
     */
    public static ConfigUtils init() {
        if (instance == null) {
            instance = new ConfigUtils();
        }
        return instance;
    }


    //获取String类型
    public String getStr(String key) {
        try {
            Object eval = JSONPath.eval(jsonConfig, SystemKey.YML_PATH_PREFIX + key);
            if (eval != null) {
                return eval.toString();
            }
            return null;
        } catch (Exception e) {
            logger.error("获取解析value失败.", e);
            return null;
        }
    }

    public String getStr(String key, String def) {
        String val = getStr(key);
        if (val == null) {
            return def;
        }
        return val;
    }

    public Integer getInt(String key) {
        String value = getStr(key);
        if (value != null) {
            return Integer.valueOf(value);
        }
        return null;
    }

    public Integer getInt(String key, int defaultValue) {
        String value = getStr(key);
        if (value != null) {
            return Integer.valueOf(value);
        }
        return defaultValue;
    }

    public Boolean getBoolean(String key) {
        String value = getStr(key);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return null;
    }

    public Boolean getBoolean(String key, boolean defaultValue) {
        String value = getStr(key);
        if (value != null) {
            return Boolean.valueOf(value);
        }
        return defaultValue;
    }

    public List<String> getList(String key) {

        return (List<String>) JSONPath.eval(jsonConfig, SystemKey.YML_PATH_PREFIX + key);
    }

    /**
     * 获取 plugin拦截的类型
     *
     * @param key
     */
    public Object getVal(String key) {
        return JSONPath.eval(jsonConfig, SystemKey.YML_PATH_PREFIX + key);
    }
}
