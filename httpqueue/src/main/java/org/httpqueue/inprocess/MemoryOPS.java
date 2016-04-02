package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IMemoryOPS;
import org.httpqueue.protocolbean.DirectQueue;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class MemoryOPS implements IMemoryOPS {
    @Override
    public void createDirectQueue(String queueName, int ttl,int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_DIRECT+"");
        jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
        jedis.hset(queueName, DirectQueue.TTL,ttl+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET,"0");
        RedisShard.returnJedisObject(jedis);
    }

    @Override
    public void createFanoutQueue(String queueName, int ttl, int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_FANOUT+"");
        jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
        jedis.hset(queueName, DirectQueue.TTL,ttl+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        RedisShard.returnJedisObject(jedis);

    }

    @Override
    public void createTopic(String queueName) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_TOPIC+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        RedisShard.returnJedisObject(jedis);
    }
}
