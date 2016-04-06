package org.client.publisher;


import org.client.publisher.intf.IPublisher;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryDirectQueueConfig;
import org.client.publisher.util.queueconfig.MemoryFanoutQueueConfig;

/**
 * Created by andilyliao on 16-4-2.
 */
//
public class MemoryQueuePublisher implements IPublisher {
    private Config config;
    public MemoryQueuePublisher(Config config){
        this.config=config;
    }

    @Override
    public void initPublisher(Config config) throws Exception {
        this.config=config;
    }
    //curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":0,"m":0,"t":86400,"h":0}}'
    public void createDirectQueue(MemoryDirectQueueConfig queueConfig)throws Exception{

    }
    public void createFanoutQueue(MemoryFanoutQueueConfig queueConfig)throws Exception{

    }
//curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":1,"h":0,"tr":0,"s":0,"ts":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
    @Override
    public void publishMessage(Message message) throws Exception {

    }
}
