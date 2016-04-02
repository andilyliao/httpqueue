package org.httpqueue.outprocess.task;

import org.httpqueue.outprocess.task.intf.IOutputRegist;
import org.httpqueue.outprocess.task.intf.IReadMemory;

/**
 * Created by andilyliao on 16-4-1.
 */
public class OutputRegist implements IOutputRegist{

    @Override
    public void registDirect(String queueName) throws Exception {
        IReadMemory iReadMemory=new ReadMemory();
        iReadMemory.registDirectQueue(queueName);
    }

    @Override
    public void registTopic(String clientID,String queueName) throws Exception {
        IReadMemory iReadMemory=new ReadMemory();
        iReadMemory.registTopic(clientID,queueName);
    }

    @Override
    public void registFanout(String clientID,String queueName) throws Exception {
        IReadMemory iReadMemory=new ReadMemory();
        iReadMemory.registFanoutQueue(clientID,queueName);
    }

    @Override
    public int getQueMode(String queName) throws Exception {
        IReadMemory iReadMemory=new ReadMemory();
        return iReadMemory.getQueMode(queName);
    }
}
