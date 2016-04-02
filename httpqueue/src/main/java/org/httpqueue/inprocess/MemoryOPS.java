package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IMemoryOPS;
import org.httpqueue.protocolbean.DirectQueue;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class MemoryOPS implements IMemoryOPS {
    @Override
    public void createDirectQueue(String queueName, int ttl,int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
        jedis.hset(queueName, DirectQueue.TTL,ttl+"");
        RedisShard.returnJedisObject(jedis);
    }
}
