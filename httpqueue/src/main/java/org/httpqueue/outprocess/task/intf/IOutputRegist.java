package org.httpqueue.outprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IOutputRegist {
    public void registDirect(String queueName)throws Exception;
    public void registTopic(String clientID,String queueName)throws Exception;
    public void registFanout(String clientID,String queueName)throws Exception;
    public int getQueMode(String queName)throws Exception;
}
