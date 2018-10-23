package com.learn.snake.common;

import com.learn.snake.config.ConfigUtils;
import com.learn.snake.util.LoggerBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author :lwy
 * @Date : 2018/10/22 17:54
 * @Description : 请求id生成器
 */
public class IdHelper {

    private static final Logger logger = LoggerBuilder.getLogger(IdHelper.class);

    private static CuratorFramework client = null;


    public static String nodeName;

    private static String rootDir = "/snake/ids/";

    private static String datePattern = "yyMMddHHmmss";

    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern);

    //初始化时间戳
    private static String timeFlag = sdf.format(new Date());

    private static String initTimeFlag = null;

    //是否初始化状态值
    private static boolean timeInitFlag = false;


    /**
     * 初始化zk客户端
     */
    public static void init() {

        logger.info("init the zk .......");
        String server = ConfigUtils.init().getStr("id.zk.url");
        if (StringUtils.isEmpty(server)) {
            logger.error("the config.yml is server is null");
            return;
        }
        Integer timeOut = ConfigUtils.init().getInt("id.zk.timeout");
        if (timeOut == null) {
            logger.error("the config.yml is timeout is null");
            return;
        }
        Integer retryTimes = ConfigUtils.init().getInt("id.zk.retryTimes");
        if (retryTimes == null) {
            logger.error("the config.yml is retryTimes is null");
            return;
        }
        Integer retrySleep = ConfigUtils.init().getInt("id.zk.retrySleep");
        if (retrySleep == null) {
            logger.error("the config.yml is retrySleep is null");
            return;
        }

        logger.info("the zookeeper server is " + server);

        //初始化zk客户端
        client = CuratorFrameworkFactory.builder()
                .connectString(server)
                .sessionTimeoutMs(1000 * 10)
                .connectionTimeoutMs(timeOut)
                .retryPolicy(new RetryNTimes(retryTimes, retrySleep))
                .build();

        //初始化监听器
        client.getConnectionStateListenable().addListener(new SnakeConnectionStateListener());
        client.start();

        logger.info("初始化分布式id");
        generateNode();
        logger.info("初始化分布式时间戳");
        initTimeTag();
        //设置表示状态
        timeInitFlag = true;
    }


    /**
     * 初始化分布式时间戳
     * 后续进行优化 TODO
     */
    private static void initTimeTag() {
        Calendar calendar = Calendar.getInstance();
        timeFlag = sdf.format(calendar.getTime());
        initTimeFlag = timeFlag;
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + 1);
        calendar.set(Calendar.MILLISECOND, 0);

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        if (!timeInitFlag) {
            //未初始化,启动后台执行时间任务
            Timer timer = new Timer(true);
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    timeFlag = sdf.format(new Date());
                }
            }, delay, 1000);
        }
    }

    /**
     * 生成分布式节点
     *
     */
    private static void generateNode() {
        nodeName = null;
        String s_start = ConfigUtils.init().getStr("id.start", "1001");
        String s_end = ConfigUtils.init().getStr("id.end", "9999");
        int start = Integer.parseInt(s_start);
        int end = Integer.parseInt(s_end);
        for (int i = start; i <= end; i++) {
            int flag = createNode(i);
            if (flag == 1) {
                //后续增加表示id的醒目表示如 i+"snake" TODO
                nodeName = i + "";
                logger.info("当前分布式节点为： " + nodeName);
                break;
            } else if (flag == -1) {
                break;
            }
        }
    }

    private static int createNode(int nNodeName) {
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(rootDir + nNodeName, buildNodeText().getBytes("utf-8"));
            return 1;
        } catch (KeeperException.NodeExistsException e) {
            return 0;
        } catch (Exception e) {
            logger.error("create node is failed ", e);
        }
        return -1;
    }

    private static String buildNodeText() {
        StringBuilder sb = new StringBuilder();
        sb.append("startTime=").append(sdf.format(new Date()));
        return sb.toString();
    }

    private static class SnakeConnectionStateListener implements org.apache.curator.framework.state.ConnectionStateListener {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            if (newState == ConnectionState.CONNECTED) {
                logger.info("zk connected established");
            } else if (newState == ConnectionState.LOST || newState == ConnectionState.RECONNECTED) {
                try {
                    client.close();
                } catch (Exception e) {
                    logger.error("close the zk connection is failed.");
                }
                try {
                    init();
                } catch (Exception e) {
                    logger.error("retry connection zk is failed.");
                }
            }
        }
    }
}
