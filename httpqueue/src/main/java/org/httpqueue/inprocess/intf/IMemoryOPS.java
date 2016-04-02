package org.httpqueue.inprocess.intf;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IMemoryOPS {
    public void createDirectQueue(String queueName,int ttl)throws Exception;
}
