package org.client.consumer;

import org.client.consumer.intf.IConsumer;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-4-2.
 */
public class Consumer implements IConsumer {
    @Override
    public void registQueue(OutputHead outputHead) throws Exception {

    }

    @Override
    public MessageBody consumeMessage(OutputHead outputHead) throws Exception {
        return null;
    }
}
