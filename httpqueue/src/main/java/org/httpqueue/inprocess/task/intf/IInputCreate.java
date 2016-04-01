package org.httpqueue.inprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IInputCreate {
    public void createDirectWithoutDisk(String queueName)throws Exception;
    public void createDirectWithDisk(String queueName)throws Exception;
    public void createTopic(String queueName)throws Exception;
    public void createFanoutWithoutDisk(String queueName)throws Exception;
    public void createFanoutWithDisk(String queueName)throws Exception;
}
