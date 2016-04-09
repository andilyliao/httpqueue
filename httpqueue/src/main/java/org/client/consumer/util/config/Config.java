package org.client.consumer.util.config;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by andilyliao on 16-4-7.
 */
public class Config extends HashMap<String,String> {
    private static Logger log = Logger.getLogger(Config.class);
    public static final String HTTPQUEUE_SERVER="urls";
    public static final String PINGNUM="pingnum";
    public static HashMap<String,Integer> listenerclustor=new HashMap<String,Integer>();
    public static HashMap<Integer,String> urlMap=new HashMap<Integer, String>();
    private static Map<Integer,JedisPool> redispools=null;
    private static Map<String,String> poolMap=new HashMap<String,String>();
    public static String MAXTOTAL="maxTotal";
    public static String MAXIDLE="maxIdle";
    public static String MAXWAITMILLIS="maxWaitMillis";
    public static String REDISTIMEOUT="redisTimeout";
    //服务地址
    public Config(){

    }
    public Config(String path) throws IOException {
        Properties prop = new Properties();
        InputStream in = this.getClass().getResourceAsStream(path);//TODO 修改成非classpath路径
        prop.load(in);
        String[] hostsandports=prop.getProperty(HTTPQUEUE_SERVER).split(",");
        log.debug("http server host and port----------------------------{");
        for(int i=0;i<hostsandports.length;i++){
            String onehostport=hostsandports[i];
            this.put(HTTPQUEUE_SERVER,onehostport);
            urlMap.put(i,onehostport);
            log.debug("http server url is: "+i+" "+onehostport);
        }
        this.put(PINGNUM,prop.getProperty(PINGNUM));
        log.debug("}----------------------------http server host and port");
        String[] listenerhosts=prop.getProperty("listenerhost").split(",");
        log.debug(listenerhosts.length);
        for(int i=0;i<listenerhosts.length;i++){
            String[] onelistenerhostport=listenerhosts[i].split(":");
            listenerclustor.put(listenerhosts[i],Integer.parseInt(onelistenerhostport[1]));
        }
        poolMap.put(MAXTOTAL,prop.getProperty("maxTotal"));
        poolMap.put(MAXIDLE,prop.getProperty("maxIdle"));
        poolMap.put(MAXWAITMILLIS,prop.getProperty("maxWaitMillis"));
        poolMap.put(REDISTIMEOUT,prop.getProperty("redisTimeout"));
    }
    public static JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(Integer.parseInt(poolMap.get(MAXTOTAL)));
        jedisPoolConfig.setMaxIdle(Integer.parseInt(poolMap.get(MAXIDLE)));
        jedisPoolConfig.setMaxWaitMillis(Integer.parseInt(poolMap.get(MAXWAITMILLIS)));
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }
    public static void initJedisPool(){
        redispools=new HashMap<Integer,JedisPool>();
        int hashmod=0;
        log.debug("one redis are:------------------------------{");
        for (String hostandport: listenerclustor.keySet()) {
            log.debug(hostandport);
            String[] onehostport=hostandport.split(":");
            redispools.put(hashmod,new JedisPool(jedisPoolConfig(),onehostport[0], listenerclustor.get(hostandport)));
            hashmod++;
        }
        log.debug("}------------------------------");
    }
    public static void distoryRedisPools(){
        for (Integer i: redispools.keySet()) {
            redispools.get(i).destroy();
        }
    }
    public static Jedis getJedis(int hashmod){
        return redispools.get(hashmod).getResource();
    }

    public static Jedis getJedis(JedisPool jedispool){
        return jedispool.getResource();
    }
    public static Map<Integer,JedisPool> getAllJedisPool(){
        return redispools;
    }
    public static JedisPool getJedisPool(int hashmod){
        return redispools.get(hashmod);
    }
    public static void returnJedisObject(Jedis jedis,int hashmod){
        redispools.get(hashmod).returnResourceObject(jedis);
    }
    public static void returnJedisObject(Jedis jedis,JedisPool jedispool){
        jedispool.returnResourceObject(jedis);
    }
}
