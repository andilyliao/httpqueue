package org.httpqueue.outprocess.task;

import org.httpqueue.outprocess.task.intf.IOutputConsume;
import org.httpqueue.protocolbean.MessageBody;

/**
 * Created by andilyliao on 16-4-1.
 */
public class OutputConsume implements IOutputConsume {
    @Override
    public MessageBody consumeMessageWithDisk(String queName, int offset, int seq) throws Exception {
        return new MessageBody();
    }

    @Override
    public MessageBody consumeMessageWithoutDisk(String queName, int offset, int seq) throws Exception {
        return new MessageBody();
    }
}
