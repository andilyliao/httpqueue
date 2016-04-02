package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IMemoryOPS;
import org.httpqueue.protocolbean.DirectQueue;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.PropertiesStr;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class MemoryOPS implements IMemoryOPS {
    @Override
    public void createDirectQueue(String queueName, int ttl,int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_DIRECT+"");
        jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
        jedis.hset(queueName, DirectQueue.TTL,ttl+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET,"0");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_YES+"");
        RedisShard.returnJedisObject(jedis);
    }

    @Override
    public void createFanoutQueue(String queueName, int ttl, int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_FANOUT+"");
        jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
        jedis.hset(queueName, DirectQueue.TTL,ttl+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_YES+"");
        RedisShard.returnJedisObject(jedis);
    }

    @Override
    public void createTopic(String queueName) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_TOPIC+"");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
        jedis.set(queueName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_YES+"");
        RedisShard.returnJedisObject(jedis);
    }

    @Override
    public void inputDirectMessage(String queName, String body,int seq) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!=Mode.MODE_DIRECT){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a direct queue,please check! queueName is: "+queName);
        }
        int ttl=Integer.parseInt(jedis.hget(queName, DirectQueue.TTL));
        Long pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
        jedis.set(key,body);
        jedis.expire(key,ttl);
        int recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        RedisShard.returnJedisObject(jedis);
        if(recive==Mode.RECIVE_NO) {
            int hashmod = queName.hashCode() % PropertiesStr.redisclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            RedisShard.returnJedisObject(onejedis, hashmod);
        }

    }

    @Override
    public void inputFanoutMessage(String queName, String body,int seq) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!=Mode.MODE_FANOUT){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a fanout queue,please check! queueName is: "+queName);
        }
        int ttl=Integer.parseInt(jedis.hget(queName, DirectQueue.TTL));
        Long pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
        jedis.set(key,body);
        jedis.expire(key,ttl);
        int recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        RedisShard.returnJedisObject(jedis);
        if(recive==Mode.RECIVE_NO) {
            int hashmod = queName.hashCode() % PropertiesStr.redisclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            RedisShard.returnJedisObject(onejedis, hashmod);
        }
    }

    @Override
    public void inputTopicMessage(String queName, String body,int seq) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!=Mode.MODE_TOPIC){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a topic queue,please check! queueName is: "+queName);
        }
        Long pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
        jedis.set(key,body);
        jedis.expire(key, PropertiesStr.topicttl);
        int recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        RedisShard.returnJedisObject(jedis);
        if(recive==Mode.RECIVE_NO) {
            int hashmod = queName.hashCode() % PropertiesStr.redisclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            RedisShard.returnJedisObject(onejedis, hashmod);
        }
    }
}
