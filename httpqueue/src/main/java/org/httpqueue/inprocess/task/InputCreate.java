package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IInputCreate;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputCreate implements IInputCreate {

    @Override
    public void createDirectWithoutDisk(String queueName) throws Exception {

    }

    @Override
    public void createDirectWithDisk(String queueName) throws Exception {

    }

    @Override
    public void createTopic(String queueName) throws Exception {

    }

    @Override
    public void createFanoutWithoutDisk(String queueName) throws Exception {

    }

    @Override
    public void createFanoutWithDisk(String queueName) throws Exception {

    }
}
