package com.learn.snake.constant;

/**
 * @Author :lwy
 * @Date : 2018/10/22 18:21
 * @Description :
 */
public class SystemKey {

    public static final String CONFIG_PATH = "/config.yml";

    public static final String YML_PATH_PREFIX = "$.";

    public static final String SNAKE_CONFIG = "snake.config";


    //-------------------------------------------------------//
    //javaagent参数
    public static String SNAKE_SERVER = "snake.server";
    public static String SNAKE_CLUSTER = "snake.cluster";
    public static String SNAKE_IP = "snake.ip";
    public static String SNAKE_PORT = "snake.port";

    //-------------------------------------------------------//

    //transmitter传输参数
    public static String TRANSMIT_FOLDER = "transmit";
    public static String TRANSMIT_DEF = "snake-transmit.def";


    //-------------------------------------------------------//
    //plugins
    public static String[] PLUGINS = new String[]{"plugins", "ext-lib"};
    public static String PLUGIN_DEF = "snake-plugin.def";
    public static String PLUGIN_FOLDER = "plugins";

    //-------------------------------------------------------//
    //采集指标子信息
    //1.开关
    public static String PROCESS_ENABLE = "plugins.process.enable";
    //2.参数采集开关
    public static String PROCESS_ENABLE_PARAM = "plugins.process.enableParam";
    //3.排除的参数类型
    public static String PROCESS_PARAMS_TYPE = "plugins.process.excludeParamTypes";
    //4.排除的参数前缀类型
    public static String PROCESS_PARAMS_TYPE_PREFIX="plugins.process.excludeParamTypePrefix";
    //5.错误开关
    public static String PROCESS_ERROR_ENABLE = "plugins.process.error.enable";
    public static String PROCESS_ERROR_INCLUDE ="plugins.process.error.includeErrorPointPrefix";
    public static String PROCESS_ERROR_EXCLUDE ="plugins.process.error.excludeErrorPointPrefix";
    //6.花费时间
    public static String PROCESS_SPEND="plugins.process.spend";

    //plugins interceptorsPoint
    public static String PLUGIN_INTERCEPTOR_POINTS = "plugins.process.interceptPoints";
    public static String PLUGIN_TYPE_MATCH = "typeMatch";
    public static String PLUGIN_METHOD_MATCH = "methodMatch";


}
