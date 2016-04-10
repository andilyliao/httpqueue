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
        //TODO 如果注册过需要处理注册过的逻辑
    }

    @Override
    public int registFanoutQueue(String clientID,String queueName) throws Exception {
        //TODO 后面需要做每个队列的注册消费者数量的监控
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
    public MessageBody outputDirect(String queName, long offset, int seq) throws Exception {
        //第一次消费不知道offset需要从多少开始的时候，设置为0就好，系统会找到匹配的数据，如果是事务，第0个取不到说明即便取数据也不完整，于是直接取下一个

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
        long pubset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            String key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            pubset = Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
            reoffset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.OFFSET));
            log.debug("key: "+key+" jedis: "+jedis);
            while(!jedis.exists(key)){//获取不到数据的时候看当前的pubset是否大于需要取数据的offset，如果pubset大于请求的offset，循环知道找到当前应该消费的存在的key，并且更新offset，如果pubset小于等于请求的offset则返回无数据的错误信息
                if(pubset<=offset){
                    log.debug("There will be no data for return!");
                    jedis.set(queName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_NO+"");
                    return new MessageBody(Mode.DATA_NO,pubset,offset,"",getseq,gettotleseq);//没有新数据，返回当前的offset
                }
                if(offset<reoffset){
                    log.debug("Input offset is too old,please add 1 and then call this method again!");
                    return new MessageBody(Mode.DATA_NO,pubset,reoffset,"",getseq,gettotleseq);//没有新数据，返回当前的pubset
                }
                log.debug("Offset was too old,system will be add 1 for it!");
                //如果有新数据则寻找新数据
                long offsettmp=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET);
                offset=offsettmp;
                key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offsettmp,seq);
            }
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            log.debug("body: "+body+" getseq: "+getseq+" gettotoleseq: "+gettotleseq);
            //如果数据是事务数据，则事务没有取完前是不能offset+1的
            if(getseq>=gettotleseq) {
                reoffset = jedis.incr(queName + CommonConst.splitor + CommonConst.OFFSET);
            }else {
                reoffset = Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.OFFSET));
            }
            jedis.del(key);
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,pubset,reoffset,body,getseq,gettotleseq);
    }

    @Override
    public MessageBody outputFanout(String clientID, String queName, long offset, int seq) throws Exception {
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
        long pubset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            pubset = Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
            reoffset=Long.parseLong(jedis.get(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID));
            while(!jedis.exists(key)){//获取不到数据的时候看当前的pubset是否大于需要取数据的offset，如果pubset大于请求的offset，循环知道找到当前应该消费的存在的key，并且更新offset，如果pubset小于等于请求的offset则返回无数据的错误信息
                if(pubset<=offset){
                    log.debug("There will be no data for return!");
                    jedis.set(queName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_NO+"");
                    return new MessageBody(Mode.DATA_NO,pubset,offset,"",getseq,gettotleseq);//没有新数据，返回当前的offset
                }
                if(offset<reoffset){
                    log.debug("Input offset is too old,please add 1 and then call this method again!");
                    return new MessageBody(Mode.DATA_NO,pubset,reoffset,"",getseq,gettotleseq);//没有新数据，返回当前的pubset
                }
                log.debug("Offset was too old,system will be add 1 for it!");
                //如果有新数据则寻找新数据
                long offsettmp=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
                offset=offsettmp;
                key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offsettmp,seq);
            }
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            log.debug("body: "+body+" getseq: "+getseq+" gettotoleseq: "+gettotleseq);
//            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            if(getseq>=gettotleseq) {
                reoffset = jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            }else {
                reoffset = Long.parseLong(jedis.get(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID));
            }
//            pubset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,pubset,reoffset,body,getseq,gettotleseq);
    }

    @Override
    public MessageBody outputTopic(String clientID, String queName, long offset, int seq) throws Exception {
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
        long pubset=0;
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        try {
            //TODO 消费者消费的数据如果ttl过期了，直接将offset设置成当前的pubset
            String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
            pubset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
            reoffset=Long.parseLong(jedis.get(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID));
            while(!jedis.exists(key)){
                if(pubset<=offset){
                    log.debug("There will be no data for return!");
                    jedis.set(queName+ CommonConst.splitor+CommonConst.RECIVE,Mode.RECIVE_NO+"");
                    return new MessageBody(Mode.DATA_NO,pubset,offset,"",getseq,gettotleseq);//没有新数据，返回当前的offset
                }
                offset=pubset;
                key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
                jedis.set(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID, String.valueOf(pubset));
            }

            log.debug("key is: "+key);
            bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
            getseq=Integer.parseInt(bodyseqandtotle[1]);
            gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
            body=bodyseqandtotle[0];
            log.debug(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
//            reoffset=jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            if(getseq>=gettotleseq) {
                reoffset = jedis.incr(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID);
            }else {
                reoffset = Long.parseLong(jedis.get(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID));
            }
        }catch(Exception e){
            ishasdata=Mode.DATA_NO;
            log.error("system error:",e);
        }finally {
            RedisShard.returnJedisObject(jedis);
        }
        return new MessageBody(ishasdata,pubset,reoffset,body,getseq,gettotleseq);
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
