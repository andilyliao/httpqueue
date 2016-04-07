package org.client.publisher.intf;

import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryDirectQueueConfig;
import org.client.publisher.util.result.CommonRes;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IPublisher {
    public void initPublisher(Config config)throws Exception;
    public CommonRes publishMessage(Message message)throws Exception;
}
