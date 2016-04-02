package org.httpqueue.outprocess;

import org.apache.log4j.Logger;
import org.httpqueue.outprocess.intf.IReadMemory;
import org.httpqueue.protocolbean.DirectQueue;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class ReadMemory implements IReadMemory {
    private static Logger log = Logger.getLogger(ReadMemory.class);
    @Override
    public void registDirectQueue(String queueName) throws Exception {
        //TODO 目前没有任务，后面需要做每个队列的注册消费者数量的监控
    }

    @Override
    public int registFanoutQueue(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
        ShardedJedis jedis = RedisShard.getJedisObject();
        String pubset="0";
        try {
            pubset = jedis.get(queueName + CommonConst.splitor + CommonConst.PUBSET);
            jedis.set(queueName + CommonConst.splitor + CommonConst.OFFSET + CommonConst.splitor + clientID, "0");
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return Integer.parseInt(pubset);
    }

    @Override
    public int registTopic(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
        ShardedJedis jedis=RedisShard.getJedisObject();
        String pubset="0";
        try {
            pubset =jedis.get(queueName+ CommonConst.splitor+CommonConst.PUBSET);
            jedis.set(queueName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID,pubset);
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return Integer.parseInt(pubset);
    }

    @Override
    public MessageBody outputDirect(String queName, int offset, int seq) throws Exception {
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
        long reoffset=0;
        String body="";
        try {
            String key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            body=jedis.get(key);
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET);
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(reoffset,body);
    }

    @Override
    public MessageBody outputFanout(String clientID, String queName, int offset, int seq) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!=Mode.MODE_FANOUT){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a direct queue,please check! queueName is: "+queName);
        }
        long reoffset=0;
        String body="";
        try {
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            body=jedis.get(key);
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(reoffset,body);
    }

    @Override
    public MessageBody outputTopic(String clientID, String queName, int offset, int seq) throws Exception {
        ShardedJedis jedis=RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!=Mode.MODE_TOPIC){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a direct queue,please check! queueName is: "+queName);
        }
        long reoffset=0;
        String body="";
        try {
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            body=jedis.get(key);
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
        }catch(Exception e){
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(reoffset,body);
    }
}
