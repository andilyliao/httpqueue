package org.httpqueue.outprocess;

import org.httpqueue.outprocess.intf.IReadMemory;
import org.httpqueue.protocolbean.DirectQueue;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class ReadMemory implements IReadMemory {
    @Override
    public void registDirectQueue(String queueName) throws Exception {
        //TODO 目前没有任务，后面需要做每个队列的注册消费者数量的监控
    }

    @Override
    public int registFanoutQueue(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
        ShardedJedis jedis=RedisShard.getJedisObject();
        String pubset=jedis.get(queueName+ CommonConst.splitor+CommonConst.PUBSET);
        jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID,"0");
        RedisShard.returnJedisObject(jedis);
        return Integer.parseInt(pubset);
    }

    @Override
    public int registTopic(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
        ShardedJedis jedis=RedisShard.getJedisObject();
        String pubset=jedis.get(queueName+ CommonConst.splitor+CommonConst.PUBSET);
        jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID,pubset);
        RedisShard.returnJedisObject(jedis);
        return Integer.parseInt(pubset);
    }
}
