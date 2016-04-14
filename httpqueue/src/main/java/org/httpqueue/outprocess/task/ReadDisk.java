package org.httpqueue.outprocess.task;

import org.httpqueue.outprocess.task.intf.IReadDisk;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.util.CommonConst;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.ShardedJedis;

/**
 * Created by andilyliao on 16-3-31.
 */
public class ReadDisk implements IReadDisk {
    @Override
    public MessageBody outputDirect(String queName, long offset, int seq) throws Exception {
        ShardedJedis jedis= RedisShard.getJedisObject();
        if(!jedis.exists(queName)){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't exsit,please check! queueName is: "+queName);
        }
        int type=Integer.parseInt(jedis.hget(queName, CommonConst.TYPE));
        if(type!= Mode.MODE_DIRECT){
            RedisShard.returnJedisObject(jedis);
            throw new Exception("This queue isn't a direct queue,please check! queueName is: "+queName);
        }
        String[] bodyseqandtotle;
        String body="";
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        String key=queName+CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
        long pubset = Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        long reoffset=Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.OFFSET));
        bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
        getseq=Integer.parseInt(bodyseqandtotle[1]);
        gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
        body=bodyseqandtotle[0];
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
        String body="";
        String[] bodyseqandtotle;
        int getseq=0;
        int gettotleseq=0;
        int ishasdata=Mode.DATA_YES;
        String key=queName+ CommonConst.splitor+CommonConst.puboffsetAndSeq(offset,seq);
        long pubset = Long.parseLong(jedis.get(queName + CommonConst.splitor + CommonConst.PUBSET));
        long reoffset=Long.parseLong(jedis.get(queName+ CommonConst.splitor+CommonConst.OFFSET+CommonConst.splitor+clientID));
        bodyseqandtotle=jedis.get(key).split(CommonConst.splitor);
        getseq=Integer.parseInt(bodyseqandtotle[1]);
        gettotleseq=Integer.parseInt(bodyseqandtotle[2]);
        body=bodyseqandtotle[0];
        return new MessageBody(ishasdata,pubset,reoffset,body,getseq,gettotleseq);
    }
}
