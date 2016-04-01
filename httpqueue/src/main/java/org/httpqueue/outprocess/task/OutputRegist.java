package org.httpqueue.outprocess.task;

import org.httpqueue.outprocess.task.intf.IOutputRegist;

/**
 * Created by andilyliao on 16-4-1.
 */
public class OutputRegist implements IOutputRegist{
    @Override
    public int getQueMode(String queueName) throws Exception {
        return 0;
    }

    @Override
    public void registDirect(String queueName) throws Exception {

    }

    @Override
    public void registTopic(String queueName) throws Exception {

    }

    @Override
    public void registFanout(String queueName) throws Exception {

    }
}
