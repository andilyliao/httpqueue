package org.client.consumer.intf;

import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IConsumer {
    public void registQueue(OutputHead outputHead)throws Exception;
    public MessageBody consumeMessage(OutputHead outputHead)throws Exception;
}
