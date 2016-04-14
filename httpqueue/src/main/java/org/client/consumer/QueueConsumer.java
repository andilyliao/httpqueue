package org.client.consumer;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.client.consumer.intf.Consume;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.config.Contral;
import org.client.consumer.util.queueconfig.QueueConfig;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;
import org.httpqueue.util.PropertiesStr;
import org.httpqueue.util.redis.RedisShard;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by andilyliao on 16-4-7.
 */
public class QueueConsumer extends Consume implements IConsumer {
    private static Logger log = Logger.getLogger(QueueConsumer.class);
    private Config config;
    private QueueConfig queueConfig;
    private Contral contral=new Contral();
    public static final String splitor="!=end=!";//redis的key的华丽的分隔符
    public static final String RECIVE="RECIVE";//消费者是否还在持续获取数据
    public static final String NOTIFY="HI";//用作通知的字符串
    public QueueConsumer(Config config) {
        this.config = config;
        this.contral.setPingnum(new AtomicInteger(Integer.parseInt(config.get(Config.PINGNUM))));
        this.contral.setCountDownLatch(new CountDownLatch(1));
        this.config.initJedisPool();
    }
    @Override
    public void initConsumer(Config config) throws Exception {
        this.config = config;
        this.contral.setPingnum(new AtomicInteger(Integer.parseInt(config.get(Config.PINGNUM))));
        this.contral.setCountDownLatch(new CountDownLatch(1));
        this.config.initJedisPool();
    }
//curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":0,"h":0}}'
    public CommonRes registConsumer(QueueConfig queueConfig) throws Exception {
        this.queueConfig=queueConfig;
        final String queueName = queueConfig.getQueueName();
        String uid=queueConfig.getUid();

        int hashmod = Math.abs(queueName.hashCode()) % this.config.listenerclustor.size();
        log.debug("------------: "+hashmod);
        System.out.println( queueName+"    "+queueName.hashCode()+"   "+hashmod+"   "+this.config.listenerclustor.size());
        final Jedis jedis=Config.getJedis(hashmod);
        final NotifyListenter notifyListenter =new NotifyListenter();
        notifyListenter.setContral(this.contral);
        notifyListenter.setPingnum(Integer.parseInt(this.config.get(Config.PINGNUM)));

        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {
                jedis.subscribe(notifyListenter,RECIVE+splitor+queueName);
            }
        });
        t.start();
        String url = this.config.urlMap.get(Math.abs(queueName.hashCode()) % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"id\":\""+uid+"\",\"ty\":0,\"h\":0}}";
        String body = send(url, json, "utf-8");
        CommonRes cr= JSON.parseObject(body, CommonRes.class);
        return cr;
    }
//curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":1,"h":0,"o":1,"s":0}}'
    @Override
    public MsgRes consumeMsg(MsgRes msgRes) throws Exception {
        //TODO计入计数器 计数器到数后await
        this.queueConfig=queueConfig;
        String queueName = queueConfig.getQueueName();
        String uid=queueConfig.getUid();
        long offset=msgRes.getOffset();
        int seq=msgRes.getSeq();
        String url = this.config.urlMap.get(Math.abs(queueName.hashCode()) % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"id\":\""+uid+"\",\"ty\":1,\"h\":0,\"o\":"+offset+",\"s\":"+seq+"}}";
        String body = send(url, json, "utf-8");
        CommonRes cr= JSON.parseObject(body, CommonRes.class);
        MsgRes res=JSON.parseObject(cr.getBody(),MsgRes.class);
        if(res.getBody().equals("")&&(res.getOffset()>=res.getPutset())){
            if(this.contral.getPingnum().decrementAndGet()<=0){
                this.contral.getCountDownLatch().await();
            }
        }
        return res;
    }
}
