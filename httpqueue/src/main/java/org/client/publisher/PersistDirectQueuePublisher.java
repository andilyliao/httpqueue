package org.client.publisher;


import org.client.publisher.intf.IPublisher;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.result.CommonRes;

/**
 * Created by andilyliao on 16-4-2.
 */
//
public class PersistDirectQueuePublisher implements IPublisher {
    private Config config;
    public PersistDirectQueuePublisher(Config config){
        this.config=config;
    }

    @Override
    public void initPublisher(Config config) throws Exception {
        this.config=config;
    }
    //curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":0,"m":0,"t":86400,"h":0}}'

//curl http://localhost:8844/queue -d '{"head":{"qn":"mydirectqueue","ty":1,"h":0,"tr":0,"s":0,"ts":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
    @Override
    public CommonRes publishMessage(Message message) throws Exception {
        return new CommonRes();
    }
}
