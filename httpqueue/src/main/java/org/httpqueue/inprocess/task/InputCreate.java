package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IInputCreate;
import org.httpqueue.inprocess.task.intf.IMemoryOPS;
import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputCreate implements IInputCreate {

    @Override
    public void createDirectWithoutDisk(String queueName,int ttl) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        iMemoryOPS.createDirectQueue(queueName,ttl, Mode.HASHDISK_WITHOUTDISK);
    }

    @Override
    public void createDirectWithDisk(String queueName,int ttl) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        iMemoryOPS.createDirectQueue(queueName,ttl, Mode.HASHDISK_WITHDISK);
    }

    @Override
    public void createTopic(String queueName) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        iMemoryOPS.createTopic(queueName);
    }

    @Override
    public void createFanoutWithoutDisk(String queueName,int ttl) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        iMemoryOPS.createFanoutQueue(queueName,ttl, Mode.HASHDISK_WITHOUTDISK);
    }

    @Override
    public void createFanoutWithDisk(String queueName,int ttl) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        iMemoryOPS.createFanoutQueue(queueName,ttl, Mode.HASHDISK_WITHDISK);
    }
}
