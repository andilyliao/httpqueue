package org.httpqueue.outprocess.task.intf;

import org.httpqueue.protocolbean.MessageBody;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IOutputConsume {
    public MessageBody consumeMessageWithDisk(String clientID,String queName, int offset, int seq)throws Exception;
    public MessageBody consumeMessageWithoutDisk(String clientID,String queName,int offset,int seq)throws Exception;
}
