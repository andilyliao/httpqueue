package org.client.consumer.intf;

import org.client.publisher.util.config.Config;
import org.client.publisher.util.result.CommonRes;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IConsumer {
    public void initPublisher(Config config)throws Exception;
}
