package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IMemoryOPS;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class MemoryOPS implements IMemoryOPS {
    @Override
    public void createDirectQueue(String queueName, int ttl) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
    }
}
