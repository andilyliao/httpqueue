package org.client.consumer;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.client.consumer.intf.Consume;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.QueueConfig;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by andilyliao on 16-4-7.
 */
public class QueueConsumer extends Consume implements IConsumer {
    private static Logger log = Logger.getLogger(QueueConsumer.class);
    private Config config;
    private QueueConfig queueConfig;
    private CountDownLatch countDownLatch=new CountDownLatch(1);;//TODO 可配置
    private AtomicInteger pingnum=null;
    public QueueConsumer(Config config) {
        this.config = config;
    }
    @Override
    public void initConsumer(Config config) throws Exception {
        this.config = config;
        this.pingnum=new AtomicInteger(Integer.parseInt(config.get(Config.PINGNUM)));
        //TODO 加入redis推送队列监听
    }
//curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":0,"h":0}}'
    @Override
    public CommonRes registConsumer(QueueConfig queueConfig) throws Exception {
        this.queueConfig=queueConfig;
        String queueName = queueConfig.getQueueName();
        String uid=queueConfig.getUid();
        String url = this.config.urlMap.get(queueName.hashCode() % this.config.urlMap.size());
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
        String url = this.config.urlMap.get(queueName.hashCode() % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"id\":\""+uid+"\",\"ty\":1,\"h\":0,\"o\":"+offset+",\"s\":"+seq+"}}";
        String body = send(url, json, "utf-8");
        CommonRes cr= JSON.parseObject(body, CommonRes.class);
        MsgRes res=JSON.parseObject(cr.getBody(),MsgRes.class);
        if(res.getBody().equals("")&&(res.getOffset()>=res.getPutset())){
            this.countDownLatch.await();
        }
        return res;
    }
}
