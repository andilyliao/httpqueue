package org.httpqueue.inprocess.task;

import org.apache.log4j.Logger;
import org.httpqueue.inprocess.task.intf.IMemoryOPS;
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
    private static Logger log = Logger.getLogger(MemoryOPS.class);
    @Override
    public void createDirectQueue(String queueName, int ttl,int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        log.debug("======createDirectQueue=======: "+queueName+" "+ttl+" "+hasdisk);
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        try {
            jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_DIRECT+"");
            jedis.hset(queueName, DirectQueue.HASDISK,hasdisk+"");
            jedis.hset(queueName, DirectQueue.TTL,ttl+"");
            jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
            jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET,"0");
            jedis.set(queueName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_YES+"");
        }catch(Exception e){
            log.error("system error:"+queueName+" "+ttl+" "+hasdisk,e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
    }

    @Override
    public void createFanoutQueue(String queueName, int ttl, int hasdisk) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        try {
            jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_FANOUT + "");
            jedis.hset(queueName, DirectQueue.HASDISK, hasdisk + "");
            jedis.hset(queueName, DirectQueue.TTL, ttl + "");
            jedis.set(queueName + CommonConst.splitor + CommonConst.PUBSET, "0");
            jedis.set(queueName + CommonConst.splitor + CommonConst.RECIVE, Mode.RECIVE_YES + "");
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
    }

    @Override
    public void createTopic(String queueName) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(jedis.exists(queueName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue is allready exsit,please check! queueName is: "+queueName);
        }
        try {
            jedis.hset(queueName, CommonConst.TYPE, Mode.MODE_TOPIC+"");
            jedis.set(queueName+ CommonConst.splitor+CommonConst.PUBSET,"0");
            jedis.set(queueName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_YES+"");
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
    }

    @Override
    public long inputDirectMessage(String queName, String body,int seq,int totleseq) throws Exception {
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
        int recive=Mode.RECIVE_NO;
        long pubset=0;
        try {
            int ttl=Integer.parseInt(jedis.hget(queName, DirectQueue.TTL));
            pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
            jedis.set(key,body+ CommonConst.splitor+seq+ CommonConst.splitor+totleseq);
            jedis.expire(key,ttl);
            recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        if(recive==Mode.RECIVE_NO) {
            int hashmod = Math.abs(queName.hashCode()) % PropertiesStr.listenerclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            try {
            onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            }catch(Exception e){
                log.error("system error:",e);
            }finally {
                RedisShard.returnJedisObject(onejedis, hashmod);
            }

        }
        return pubset;

    }

    @Override
    public long inputFanoutMessage(String queName, String body,int seq,int totleseq) throws Exception {
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
        int recive=Mode.RECIVE_NO;
        long pubset=0;
        try {
            int ttl=Integer.parseInt(jedis.hget(queName, DirectQueue.TTL));
            pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
            jedis.set(key,body+ CommonConst.splitor+seq+ CommonConst.splitor+totleseq);
            jedis.expire(key,ttl);
            recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        if(recive==Mode.RECIVE_NO) {
            int hashmod = Math.abs(queName.hashCode()) % PropertiesStr.listenerclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            try {
                onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            }catch(Exception e){
                log.error("system error:",e);
            }finally {
                RedisShard.returnJedisObject(onejedis, hashmod);
            }
        }
        return pubset;
    }

    @Override
    public void inputTopicMessage(String queName, String body,int seq,int totleseq) throws Exception {
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
        int recive=Mode.RECIVE_NO;
        try {
            Long pubset=jedis.incr(queName+ CommonConst.splitor+CommonConst.PUBSET);
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(pubset,seq);
            jedis.set(key,body+ CommonConst.splitor+seq+ CommonConst.splitor+totleseq);
            log.debug("topic ttl is: "+PropertiesStr.topicttl);
            jedis.expire(key, PropertiesStr.topicttl);
            recive=Integer.parseInt(jedis.get(queName+ CommonConst.splitor+CommonConst.RECIVE));
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        if(recive==Mode.RECIVE_NO) {
            int hashmod = Math.abs(queName.hashCode()) % PropertiesStr.listenerclustor.size();
            Jedis onejedis = RedisShard.getJedis(hashmod);
            try {
                onejedis.publish(CommonConst.RECIVE+CommonConst.splitor+queName,CommonConst.NOTIFY);
            }catch(Exception e){
                log.error("system error:",e);
            }finally {
                RedisShard.returnJedisObject(onejedis, hashmod);
            }
        }
    }

    @Override
    public int getQueMode(String queName) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        try{
            return Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        throw new Exception("system error:");
    }
}
