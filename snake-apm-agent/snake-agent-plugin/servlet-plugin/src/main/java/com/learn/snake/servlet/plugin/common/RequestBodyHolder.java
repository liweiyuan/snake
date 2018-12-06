package com.learn.snake.servlet.plugin.common;

import com.learn.snake.common.SnakeTraceContext;


/**
 * 请求body封装
 */
public class RequestBodyHolder {
    private static ThreadLocal<ReqBody> BODY_HOLDER = new ThreadLocal<ReqBody>();

    public static String getRequestBody() {
        ReqBody body = BODY_HOLDER.get();
        if (body == null) {
            return null;
        }
        return body.body;
    }

    public static void setRequestBody(String body) {
        BODY_HOLDER.set(new ReqBody(SnakeTraceContext.getGId(), body));
    }

    public static String getGid() {
        ReqBody body = BODY_HOLDER.get();
        if (body == null) {
            return null;
        }
        return body.gid;
    }

    public static void remove() {
        BODY_HOLDER.remove();
    }

    private static class ReqBody {
        String gid;
        String body;

        ReqBody(String gid, String body) {
            this.gid = gid;
            this.body = body;
        }
    }
}
