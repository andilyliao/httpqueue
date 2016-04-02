package org.httpqueue.util.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.httpqueue.util.PropertiesStr;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.httpqueue.util.PropertiesStr.redisclustor;

/**
 * Created by andilyliao on 16-4-2.
 */
public class RedisShard {
    public static ShardedJedisPool shardedredispool=null;
    public static Map<String,JedisPool> redispools=null;
    public static JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(PropertiesStr.maxTotal);
        jedisPoolConfig.setMaxIdle(PropertiesStr.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(PropertiesStr.maxWaitMillis);
        jedisPoolConfig.setTestOnBorrow(true);
        return jedisPoolConfig;
    }
    public static ShardedJedis getJedisObject(){
        return shardedredispool.getResource();
    }
    public static void returnJedisObject(ShardedJedis jedis){
        shardedredispool.returnResource(jedis);
    }
    public static void distoryRedisShard(){
        shardedredispool.destroy();
    }
    public static void initJedisPool(){
        redispools=new HashMap<String,JedisPool>();
        for (String host: redisclustor.keySet()) {
            redispools.put(host,new JedisPool(jedisPoolConfig(),host, redisclustor.get("host")));
        }
    }
    public static void initRedisShard() {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        for (String host: redisclustor.keySet()) {
            JedisShardInfo si = new JedisShardInfo(host, redisclustor.get("host"),host);
        }
        shardedredispool = new ShardedJedisPool(jedisPoolConfig(), shards);
    }

}
