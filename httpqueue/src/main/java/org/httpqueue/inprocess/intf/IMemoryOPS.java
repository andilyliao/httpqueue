package org.httpqueue.inprocess.intf;

/**
 * Created by andilyliao on 16-3-31.
 */
//log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);
public interface IMemoryOPS {
    public void createDirectQueue(String queueName, int ttl,int hasdisk)throws Exception;
    public void createFanoutQueue(String queueName, int ttl,int hasdisk)throws Exception;
    public void createTopic(String queueName)throws Exception;
}
