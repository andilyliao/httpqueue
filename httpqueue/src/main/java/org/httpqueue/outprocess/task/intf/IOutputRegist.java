package org.httpqueue.outprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IOutputRegist {
    public int getQueMode(String queueName)throws Exception;
    public void registDirect(String queueName)throws Exception;
    public void registTopic(String queueName)throws Exception;
    public void registFanout(String queueName)throws Exception;
}
