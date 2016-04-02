package org.httpqueue.util;

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
    //redis链接
    public static HashMap<String,Integer> redisclustor;
    /**
     * redis池
     */
    public static int maxTotal=200;
    public static int maxIdle=10;
    public static int maxWaitMillis=10001;
    public static int redisTimeout=1000;
    public void initProperties() throws IOException {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream("/httpqueue.properties");
        prop.load(in);
        PropertiesStr.inBound=Integer.parseInt(prop.getProperty("InBound"));
        PropertiesStr.outBound=Integer.parseInt(prop.getProperty("OutBound"));
        String[] redishostsandports=prop.getProperty("redishost").split(",");
        for(int i=0;i<redishostsandports.length;i++){
            String[] onehostport=redishostsandports[i].split(":");
            redisclustor.put(onehostport[0],Integer.parseInt(onehostport[1]));
        }
        PropertiesStr.maxTotal=Integer.parseInt(prop.getProperty("maxTotal"));
        PropertiesStr.maxIdle=Integer.parseInt(prop.getProperty("maxIdle"));
        PropertiesStr.maxWaitMillis=Integer.parseInt(prop.getProperty("maxWaitMillis"));
        PropertiesStr.redisTimeout=Integer.parseInt(prop.getProperty("redisTimeout"));


    }
}
