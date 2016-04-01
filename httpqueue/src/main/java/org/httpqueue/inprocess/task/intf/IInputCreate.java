package org.httpqueue.inprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
public interface IInputCreate {
    public void createDirectWithoutDisk()throws Exception;
    public void createDirectWithDisk()throws Exception;
    public void createTopic()throws Exception;
    public void createFanoutWithoutDisk()throws Exception;
    public void createFanoutWithDisk()throws Exception;
}
