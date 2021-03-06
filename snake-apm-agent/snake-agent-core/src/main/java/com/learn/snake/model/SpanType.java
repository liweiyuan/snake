package com.learn.snake.model;

/**
 * @Author :lwy
 * @Date : 2018/10/25 11:14
 * @Description : Span：埋点
 * 埋点类型
 */
public class SpanType {
    public final static String PROCESS = "proc";
    public final static String SQL = "sql";
    public final static String SQL_PARAM = "sqlp";
    public final static String PARAM = "para";
    public final static String REQUEST = "req";
    public final static String LOGGER = "log";
    public final static String SPRING_TX = "tx";
    public final static String ERROR = "err";
    public final static String REQUEST_PARAM = "rp";
    public final static String REQUEST_BODY = "reqb";
    public final static String REQUEST_HEADERS ="reqh";
    public final static String RESPONSE_BODY = "resb";
}
