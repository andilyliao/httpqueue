package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IInputPublish;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputPublish implements IInputPublish {

    @Override
    public void inputMessageWithDisk(String queName, String body,  int seq) throws Exception {

    }

    @Override
    public void inputMessageWithDisk(String queName, String body) throws Exception {

    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body,  int seq) throws Exception {

    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body) throws Exception {

    }

    /**
     * 用户发布消息后，通知消费者开始轮循
     * @throws Exception
     */
    private void pubNotify() throws Exception{

    }
}
