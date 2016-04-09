package org.httpqueue.util.redis;

import org.apache.log4j.Logger;
import org.httpqueue.util.PropertiesStr;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.httpqueue.util.PropertiesStr.redisclustor;
import static org.httpqueue.util.PropertiesStr.listenerclustor;
/**
 * Created by andilyliao on 16-4-2.
 */
public class RedisShard {
    private static Logger log = Logger.getLogger(RedisShard.class);
    private static ShardedJedisPool shardedredispool=null;
    private static Map<Integer,JedisPool> redispools=null;
    public static JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(PropertiesStr.maxTotal);
        jedisPoolConfig.setMaxIdle(PropertiesStr.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(PropertiesStr.maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }
    //分布式
    public static void initRedisShard() {

        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        log.debug("shard redis are:------------------------------{");
        for (String hostandport: redisclustor.keySet()) {
            log.debug(hostandport);
            String[] onehostport=hostandport.split(":");
            JedisShardInfo si = new JedisShardInfo(onehostport[0], redisclustor.get(hostandport),hostandport);
            shards.add(si);
        }
        log.debug("}------------------------------");
        shardedredispool = new ShardedJedisPool(jedisPoolConfig(), shards);
    }
    public static void distoryRedisShard(){
        shardedredispool.destroy();
    }
    public static ShardedJedis getJedisObject(){
        return shardedredispool.getResource();
    }
    public static void returnJedisObject(ShardedJedis jedis){
        shardedredispool.returnResourceObject(jedis);
    }
    //单机
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
