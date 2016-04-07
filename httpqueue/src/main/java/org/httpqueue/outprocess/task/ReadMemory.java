package org.httpqueue.outprocess.task;

import org.apache.log4j.Logger;
import org.httpqueue.outprocess.task.intf.IReadMemory;
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
        //TODO regist需要返回当前的pubset，以及offset
        //TODO 如果注册过需要处理注册过的逻辑
    }

    @Override
    public int registFanoutQueue(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
        //TODO regist需要返回当前的pubset，offset=0
        //TODO 如果注册过需要处理注册过的逻辑
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
        //TODO regist需要返回当前的pubset，虽然返回offset，但是offset=pubset
        //TODO 如果注册过需要处理注册过的逻辑
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
        //加入业务逻辑，消费过需要删除数据
        //获取不到数据的时候看当前的pubset是否大于需要取数据的offset，如果pubset大于请求的offset，设置reoffset为pubset，如果pubset小于等于请求的offset则返回无数据的错误信息
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
        long putset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            String key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            log.debug("key: "+key+" jedis: "+jedis);
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            log.debug("body: "+body+" getseq: "+getseq+" gettotoleseq: "+gettotleseq);
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET);
            putset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,putset,reoffset,body,getseq,gettotleseq);
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
        if(!jedis.exists(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This client is not regist yet: "+clientID);
        }
        long reoffset=0;
        long putset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            putset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,putset,reoffset,body,getseq,gettotleseq);
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
        if(!jedis.exists(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This client is not regist yet: "+clientID);
        }
        long reoffset=0;
        long putset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            //TODO 消费者消费的数据如果ttl过期了，就无法对自己的offset+1 这个是bug
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            log.debug("key is: "+key);
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            log.debug(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            putset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,putset,reoffset,body,getseq,gettotleseq);
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
