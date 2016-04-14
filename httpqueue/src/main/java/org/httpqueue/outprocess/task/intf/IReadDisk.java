package org.httpqueue.outprocess.task.intf;

import org.httpqueue.protocolbean.MessageBody;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IReadDisk {
    public MessageBody outputDirect(String queName, long offset, int seq)throws Exception;//返回消息内容
    public MessageBody outputFanout(String clientID, String queName, long offset, int seq)throws Exception;//返回消息内容
}
