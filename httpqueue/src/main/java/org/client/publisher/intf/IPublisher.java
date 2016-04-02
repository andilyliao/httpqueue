package org.client.publisher.intf;

import org.httpqueue.protocolbean.InputHead;
import org.httpqueue.protocolbean.MessageBody;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IPublisher {
    public void createQueue(InputHead inputHead)throws Exception;
    public void publishMessage(InputHead inputHead, MessageBody messageBody)throws Exception;
}
