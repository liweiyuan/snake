package com.learn.snake.model;

import com.learn.snake.config.SnakeConfig;

import java.util.Date;

/**
 * @Author :lwy
 * @Date : 2018/10/23 10:49
 * @Description :
 */
public class Span {

    private Tags tags=new Tags();
    private String type;
    private Date time;
    private String server;
    private String cluster;
    private String pid;
    private String gid;
    private String id;
    private Long spend;
    private String port;
    private String ip;


    public Span(String type) {
        setType(type);
        setTime(new Date());
        setIp(SnakeConfig.init().getIp());
        setPort(SnakeConfig.init().getPort());
        setServer(SnakeConfig.init().getServer());
        setCluster(SnakeConfig.init().getCluster());
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getSpend() {
        return spend;
    }

    public void setSpend(Long spend) {
        this.spend = spend;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
