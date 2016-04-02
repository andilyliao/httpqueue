package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IMemoryOPS;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class MemoryOPS implements IMemoryOPS {
    @Override
    public void createDirectQueue(String queueName, int ttl) throws Exception {

    }
}
