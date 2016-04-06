package org.client.publisher.intf;

import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.client.publisher.util.Config;
import org.client.publisher.util.Message;
import org.client.publisher.util.QueueConfig;
import org.httpqueue.protocolbean.InputHead;
import org.httpqueue.protocolbean.MessageBody;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IPublisher {
    public void initPublisher(Config config)throws Exception;
    public void createQueue(QueueConfig queueConfig)throws Exception;
    public void publishMessage(Message message)throws Exception;
}
