package org.httpqueue.util;

import org.apache.log4j.Logger;
import org.httpqueue.util.zk.ServerDiscovery;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by andilyliao on 16-3-31.
 */
public class PropertiesStr {
    //生产者链接的地址
    public static int inBound;
    //消费者链接的地址
    public static int outBound;
    //运维管理链接的地址
    public static int manager;
    //通知消息链接的地址
    public static int notify;
    //redis链接
    public static HashMap<String,Integer> redisclustor=new HashMap<String,Integer>();
    //listener链接
    public static HashMap<String,Integer> listenerclustor=new HashMap<String,Integer>();
    //topic消息过期时间
    public static int topicttl=300;
    //leveldb存储地址
    public static String storePath="/tmp/";
    //存储缓存大小
    public static int pushMemoryPool=536870912;
    public static int cacheSize=104857600;
    /**
     * redis池
     */
    public static int maxTotal=200;
    public static int maxIdle=10;
    public static int maxWaitMillis=10001;
    public static int redisTimeout=1000;
    private static Logger log = Logger.getLogger(PropertiesStr.class);
    public void initProperties() throws IOException {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/httpqueue.properties");
        prop.load(in);
        PropertiesStr.inBound=Integer.parseInt(prop.getProperty("InBound"));
        PropertiesStr.outBound=Integer.parseInt(prop.getProperty("OutBound"));
        PropertiesStr.manager=Integer.parseInt(prop.getProperty("Manager"));
        PropertiesStr.notify=Integer.parseInt(prop.getProperty("Notify"));

        String[] redishostsandports=prop.getProperty("redishost").split(",");
        log.debug(redishostsandports.length);
        for(int i=0;i<redishostsandports.length;i++){
            String[] onehostport=redishostsandports[i].split(":");
            redisclustor.put(redishostsandports[i],Integer.parseInt(onehostport[1]));
        }
        //listenerhost
        String[] listenerhosts=prop.getProperty("listenerhost").split(",");
        log.debug(listenerhosts.length);
        for(int i=0;i<listenerhosts.length;i++){
            String[] onelistenerhostport=listenerhosts[i].split(":");
            listenerclustor.put(listenerhosts[i],Integer.parseInt(onelistenerhostport[1]));
        }
        PropertiesStr.maxTotal=Integer.parseInt(prop.getProperty("maxTotal"));
        PropertiesStr.maxIdle=Integer.parseInt(prop.getProperty("maxIdle"));
        PropertiesStr.maxWaitMillis=Integer.parseInt(prop.getProperty("maxWaitMillis"));
        PropertiesStr.redisTimeout=Integer.parseInt(prop.getProperty("redisTimeout"));
        PropertiesStr.topicttl=Integer.parseInt(prop.getProperty("topicttl"));
        PropertiesStr.storePath=prop.getProperty("storePath");
        PropertiesStr.pushMemoryPool=Integer.parseInt(prop.getProperty("pushMemoryPool"));
        PropertiesStr.cacheSize=Integer.parseInt(prop.getProperty("cacheSize"));

        ServerDiscovery.SESSION_TIMEOUT=Integer.parseInt(prop.getProperty("sessionTimeout"));
        ServerDiscovery.CONNECTION_STRING=prop.getProperty("connectionString");
        ServerDiscovery.ZK_PATH=prop.getProperty("zkPath");
        ServerDiscovery.HOSTNAME=prop.getProperty("HostName");

    }
}
