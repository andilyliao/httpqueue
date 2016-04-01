package org.httpqueue.outprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IOutputConsume {
    public String consumeMessageWithDisk(String queName,int offset,int seq)throws Exception;
    public String consumeMessageWithoutDisk(String queName,int offset,int seq)throws Exception;
}
