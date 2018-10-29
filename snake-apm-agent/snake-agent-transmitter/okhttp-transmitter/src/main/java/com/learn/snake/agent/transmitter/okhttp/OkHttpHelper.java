package com.learn.snake.agent.transmitter.okhttp;

import com.learn.snake.config.ConfigUtils;
import com.learn.snake.util.LoggerBuilder;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author :lwy
 * @Date : 2018/10/23 16:30
 * @Description :
 * <p>
 * TODO 修改为异步的服务，消费队列的形式
 * 思路时通过BlockingQueue存储记录，消费用线程池消费
 */
public class OkHttpHelper {

    private static final Logger logger = LoggerBuilder.getLogger(OkHttpHelper.class);

    private static OkHttpHelper instance;
    private static OkHttpClient client;
    private static final String keyPrefix = "transmitter.okhttp.";

    private static List<String> urlList;
    private static AtomicInteger counter = new AtomicInteger(0);

    private OkHttpHelper() {
        int connectTimeout = ConfigUtils.init().getInt(keyPrefix + "connectTimeout", 20);
        int writeTimeout = ConfigUtils.init().getInt(keyPrefix + "writeTimeout", 10);
        int readTimeout = ConfigUtils.init().getInt(keyPrefix + "readTimeout", 10);
        client = new OkHttpClient
                .Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .build();
        urlList = ConfigUtils.init().getList(keyPrefix + "url");

    }

    static OkHttpHelper getInstance() {
        if (null == instance) {
            synchronized (OkHttpHelper.class) {
                if (instance == null) {
                    instance = new OkHttpHelper();
                }
            }
        }
        return instance;
    }

    private String parseContentType(Map<String, String> header) {
        if (header == null || header.isEmpty()) {
            return "application/json; charset=utf-8";
        }
        if (header.containsKey("Content-Type")) {
            return header.get("Content-Type");
        } else if (header.containsKey("content-type")) {
            return header.get("content-type");
        } else if (header.containsKey("contentType")) {
            return header.get("contentType");
        }
        return "application/json; charset=utf-8";
    }

    /**
     * 默认json请求，其它类型请在header里添加Content-Type
     *
     * @param uri
     * @param parameters
     * @param header
     * @param content
     * @param timeout
     * @param charset
     * @return
     */
    private void post(String uri, Map<String, String> parameters, Map<String, String> header, String content, Integer timeout, String charset) {
        try {
            if (content == null) {
                content = "";
            }
            OkHttpClient myClient = client;
            String contentType = parseContentType(header);

            RequestBody body = RequestBody.create(MediaType.parse(contentType), content);
            uri = parseUrlParams(uri, parameters, charset);

            Request.Builder builder = new Request.Builder().post(body).url(uri);
            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            if (timeout != null && timeout > 0 && timeout != myClient.readTimeoutMillis() / 1000) {
                myClient = myClient.newBuilder().readTimeout(timeout, TimeUnit.SECONDS).build();
            }

            Response response = myClient.newCall(builder.build()).execute();
            if (response.isSuccessful()) {
                logger.info("okhttp上报数据成功");
            } else {
                throw new RuntimeException("Unexpected code " + response);
            }
        } catch (Exception e) {
            logger.error("okhttp上报数据失败。。。。", e);
        }
    }

    private String parseUrlParams(String uri, Map<String, String> parameters, String charset) throws Exception {
        if (null != parameters && !parameters.isEmpty()) {
            String split = "?";
            if (uri.contains("?")) {
                split = "&";
            }
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                uri += (split + URLEncoder.encode(entry.getKey(), charset) + "=" + URLEncoder.encode(entry.getValue(), charset));
                split = "&";
            }
        }
        return uri;
    }

    private String getUrl() {
        //采用轮询策略
        int i = counter.incrementAndGet() % urlList.size();
        return urlList.get(i);
    }

    void post(String body) {
        post(getUrl(), null, null, body, null, null);
    }

}
