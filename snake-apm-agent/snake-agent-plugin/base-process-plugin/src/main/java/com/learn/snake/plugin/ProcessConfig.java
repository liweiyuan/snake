package com.learn.snake.plugin;

import com.learn.snake.config.ConfigUtils;
import com.learn.snake.constant.SystemKey;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author :lwy
 * @Date : 2018/10/24 17:27
 * @Description :
 */
public class ProcessConfig {

    //采集开关
    private  Boolean enable;
    //参数采集开关
    private  Boolean enableParam;

    //排除的参数类型
    private Set<String> excludeParameTypes = new HashSet<String>();

    //排除的参数前缀类型
    private  Set<String> excludeParamsPrefixTypes = new HashSet<String>();

    //错误采集开关
    private Boolean enableError;

    private Set<String> includeErrorPointPrefix = new HashSet<String>();
    private Set<String> excludeErrorPointPrefix = new HashSet<String>();

    //花费时间
    private long spend;

    private static ProcessConfig instance;

    public static ProcessConfig init() {
        if (instance == null) {
            synchronized (ProcessConfig.class) {
                if (instance == null) {
                    instance = new ProcessConfig();
                }
            }
        }
        return instance;
    }

    //构造器
    private ProcessConfig() {
        initConfig();
    }

    /**
     * 初始化系统参数
     */
    private void initConfig() {

        //clear相关缓存
        excludeParameTypes.clear();
        excludeParamsPrefixTypes.clear();
        includeErrorPointPrefix.clear();
        excludeErrorPointPrefix.clear();

        //采集开关初始化
        enable = ConfigUtils.init().getBoolean(SystemKey.PROCESS_ENABLE, true);
        //参数采集开关
        enableParam = ConfigUtils.init().getBoolean(SystemKey.PROCESS_ENABLE_PARAM, true);
        //排除的参数类型
        List<String> excludeParamsTypeList = ConfigUtils.init().getList(SystemKey.PROCESS_PARAMS_TYPE);
        if (excludeParamsTypeList != null && !excludeParamsTypeList.isEmpty()) {
            excludeParameTypes.addAll(excludeParamsTypeList);
        }
        //排除的参数前缀类型
        List<String> excludeParamsTypePrefixs = ConfigUtils.init().getList(SystemKey.PROCESS_PARAMS_TYPE_PREFIX);
        if (excludeParamsTypePrefixs != null && !excludeParamsTypePrefixs.isEmpty()) {
            excludeParamsPrefixTypes.addAll(excludeParamsTypePrefixs);
        }
        //错误采集
        enableError=ConfigUtils.init().getBoolean(SystemKey.PROCESS_ERROR_ENABLE, true);
        List<String> includeErrorPointList = ConfigUtils.init().getList(SystemKey.PROCESS_ERROR_INCLUDE);
        if (includeErrorPointList != null && !includeErrorPointList.isEmpty()) {
            includeErrorPointPrefix.addAll(includeErrorPointList);
        }
        List<String> excludeErrorPointList = ConfigUtils.init().getList(SystemKey.PROCESS_ERROR_EXCLUDE);
        if (excludeErrorPointList != null && !excludeErrorPointList.isEmpty()) {
            excludeErrorPointPrefix.addAll(excludeErrorPointList);
        }

        spend=ConfigUtils.init().getInt(SystemKey.PROCESS_SPEND,-1);
    }

    public boolean isEnableParam(){
        return enableParam;
    }

    public boolean isEnable(){
        return enable;
    }

    public boolean isEnableError(){
        return enableError;
    }

    public boolean isExcludeParamType(Class clazz){
        String name = clazz.getName();
        if(excludeParameTypes.contains(name)){
            return true;
        }
        for (String prefix : excludeParamsPrefixTypes) {
            if (name.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    public long getSpend() {
        return spend;
    }

    public boolean checkErrorPoint(String point){
        if(!enableError){
            return false;
        }
        //黑名单，优先级别最高
        for (String prefix : excludeErrorPointPrefix) {
            if (point.startsWith(prefix)) {
                return false;
            }
        }
        //白名单
        for (String prefix : includeErrorPointPrefix) {
            if (point.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

}
