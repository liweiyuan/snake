package com.learn.snake.servlet.plugin;

import com.learn.snake.config.AbstractSnakeConfig;
import com.learn.snake.config.ConfigUtils;
import com.learn.snake.config.SnakeConfigFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @Author :lwy
 * @Date : 2018/10/31 16:21
 * @Description :
 */
public class ServletConfig extends AbstractSnakeConfig {

    private static ServletConfig instance;

    //servlet开关
    private Boolean enable;

    //servlet request开关
    private Boolean enableReqParam;

    //requestBody参数
    private Boolean enableReqBody;

    //header参数
    private Boolean enableReqHeaders;

    //responseBody参数
    private Boolean enableRespBody;

    //花费的时间
    private long spend;

    //url请求后缀排除
    private List<String> urlSuffixExcludeList;

    public static ServletConfig init() {
        if (instance == null) {
            synchronized (ServletConfig.class) {
                if (instance == null) {
                    instance = new ServletConfig();

                    //添加到配置信息中
                    SnakeConfigFactory.init().registerConfig("servlet", instance);
                }
            }
        }
        return instance;
    }

    public ServletConfig() {
        initConfig();
    }

    @Override
    public void initConfig() {
        //
        enable = ConfigUtils.init().getBoolean("plugins.servlet.enable", true);
        enableReqParam = enable & ConfigUtils.init().getBoolean("plugins.servlet.enableReqParam", false);
        enableReqBody = enable & ConfigUtils.init().getBoolean("plugins.servlet.enableReqBody", false);
        enableReqHeaders = enable & ConfigUtils.init().getBoolean("plugins.servlet.enableReqHeaders", false);
        enableRespBody = enable & ConfigUtils.init().getBoolean("plugins.servlet.enableRespBody", false);
        urlSuffixExcludeList = Arrays.asList(ConfigUtils.init().getStr("plugins.servlet.urlSuffixExclude", "").split(","));
        //http入口的耗时要小于等于方法的耗时，否则会造成调用链断开
        long processSpend = ConfigUtils.init().getInt("plugins.process.spend", -1);
        spend = ConfigUtils.init().getInt("plugins.servlet.spend", -1);
        if (spend > processSpend) {
            spend = processSpend;
        }
    }

    public long getSpend() {
        return spend;
    }

    public Boolean isEnable() {
        return enable;
    }
    public Boolean isEnableReqParam() {
        return enableReqParam;
    }
    public Boolean isEnableReqBody() {
        return enableReqBody;
    }
    public Boolean isEnableReqHeaders() {
        return enableReqHeaders;
    }
    public Boolean isEnableRespBody() {
        return enableRespBody;
    }
    public Boolean checkUrlSuffixExclude(String url){
        if(urlSuffixExcludeList == null || urlSuffixExcludeList.isEmpty()){
            return false;
        }
        for (String anUrlSuffixExcludeList : urlSuffixExcludeList) {
            if (url.endsWith(anUrlSuffixExcludeList)) {
                return true;
            }
        }
        return false;
    }
}
