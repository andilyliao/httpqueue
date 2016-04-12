package org.client.consumer;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.client.consumer.intf.Consume;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.QueueConfig;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;


/**
 * Created by andilyliao on 16-4-7.
 */
public class persistQueueConsumer extends Consume implements IConsumer {
    private static Logger log = Logger.getLogger(persistQueueConsumer.class);
    private Config config;
    private QueueConfig queueConfig;
    public persistQueueConsumer(Config config) {
        this.config = config;
    }
    @Override
    public void initConsumer(Config config) throws Exception {
        this.config = config;
    }
//curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":0,"h":0}}'
    @Override
    public CommonRes registConsumer(QueueConfig queueConfig) throws Exception {
        this.queueConfig=queueConfig;
        String queueName = queueConfig.getQueueName();
        String url = this.config.urlMap.get(Math.abs(queueName.hashCode()) % this.config.urlMap.size());
        String json = "";
        String body = send(url, json, "utf-8");
        CommonRes cr= JSON.parseObject(body, CommonRes.class);
        return cr;
    }
//curl http://localhost:8845/queue -d '{"head":{"qn":"mydirectqueue","id":"uuid","ty":1,"h":0,"o":1,"s":0}}'
    @Override
    public MsgRes consumeMsg(MsgRes msgRes) throws Exception {
        return null;
    }
}
