package org.httpqueue.outprocess.intf;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IReadMemory {
    public void registDirectQueue(String queueName)throws Exception;
    public int registFanoutQueue(String clientID,String queueName)throws Exception;//返回当前pubset
    public int registTopic(String clientID,String queueName)throws Exception;//返回当前pubset
    public String outputDirect(String queName,int offset,int seq)throws Exception;//返回消息内容
    public String outputFanout(String clientID,String queName,int offset,int seq)throws Exception;//返回消息内容
    public String outputTopic(String clientID,String queName,int offset,int seq)throws Exception;//返回消息内容

}
