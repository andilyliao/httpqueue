package org.client.publisher;


import org.apache.log4j.Logger;
import org.client.publisher.intf.IPublisher;
import org.client.publisher.intf.Publish;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryDirectQueueConfig;
import org.client.publisher.util.queueconfig.MemoryFanoutQueueConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andilyliao on 16-4-2.
 */
//
public class MemoryDirectQueuePublisher extends Publish implements IPublisher {
    private static Logger log = Logger.getLogger(MemoryDirectQueuePublisher.class);
    private Config config;

    public MemoryDirectQueuePublisher(Config config) {
        this.config = config;
    }

    @Override
    public void initPublisher(Config config) throws Exception {
        this.config = config;
    }

    //curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":0,"m":0,"t":86400,"h":0}}'
    public String createDirectQueue(MemoryDirectQueueConfig queueConfig) throws Exception {
        String queueName = queueConfig.getQueueName();
        int ttl = queueConfig.getTtl();
        String url = this.config.urlMap.get(queueName.hashCode() % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"ty\":0,\"m\":0,\"t\":"+ttl+",\"h\":0}}";
        String body = send(url, json, "utf-8");
        return body;
    }

    //curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":1,"h":0,"tr":0,"s":0,"ts":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
    @Override
    public String publishMessage(Message message) throws Exception {
        return "";
    }


}
