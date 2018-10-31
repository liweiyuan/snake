package com.learn.snake.common;

import com.learn.snake.model.Span;

/**
 * @Author :lwy
 * @Date : 2018/10/25 11:36
 * @Description : trace跟踪上下文
 */
public class SnakeTraceContext {

    private static final ThreadLocal<String> localGId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localPId = new ThreadLocal<String>();
    private static final ThreadLocal<String> localCTag = new ThreadLocal<String>();

    //清空操作
    public static void clearAll() {
        localGId.remove();
        localPId.remove();
        localCTag.remove();
    }

    public static String getPId() {
        return localPId.get();
    }

    public static void setPId(String pId) {
        localPId.set(pId);
    }

    public static String getGId() {
        String gid = localGId.get();
        if (gid == null) {
            //从zookeeper中获取
            gid = IdHelper.getId();
            setGid(gid);
        }
        return gid;
    }

    public static void setGid(String gid) {
        localGId.set(gid);
    }

    public static String getCTag(){
        return localCTag.get();
    }

    public static void setCTag(String ctag){
        localCTag.set(ctag);
    }

    public static String getCurrentId(){
        Span span = SpanManager.getCurrentSpan();
        if(span == null){
            return "nvl";
        }
        return span.getId();
    }
}
