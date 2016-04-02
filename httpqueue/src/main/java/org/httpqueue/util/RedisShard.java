package org.httpqueue.util;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

import static org.httpqueue.util.PropertiesStr.redisclustor;

public class RedisShard {
	public static ShardedJedisPool redispool=null;
	public static void initRedisShard() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		for (String host: redisclustor.keySet()) {
			JedisShardInfo si = new JedisShardInfo(host, redisclustor.get("host"),host);
		}
		redispool = new ShardedJedisPool(new GenericObjectPoolConfig(), shards);
	}
	public static ShardedJedis getJedisObject(){
		return redispool.getResource();
	}
	public static void returnJedisObject(ShardedJedis jedis){
		redispool.returnResource(jedis);
	}
	public static void distoryRedisShard(){
		redispool.destroy();
	}
}
