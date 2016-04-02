package org.httpqueue.inprocess.task.intf;

/**
 * Created by andilyliao on 16-3-31.
 */
//log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);
public interface IMemoryOPS {
    public void createDirectQueue(String queueName, int ttl,int hasdisk)throws Exception;
    public void createFanoutQueue(String queueName, int ttl,int hasdisk)throws Exception;
    public void createTopic(String queueName)throws Exception;
    public void inputDirectMessage(String queName,String body,int seq)throws Exception;
    public void inputFanoutMessage(String queName,String body,int seq)throws Exception;
    public void inputTopicMessage(String queName,String body,int seq)throws Exception;
}
