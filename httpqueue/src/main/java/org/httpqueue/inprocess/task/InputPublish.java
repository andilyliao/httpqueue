package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IInputPublish;
import org.httpqueue.inprocess.task.intf.IMemoryOPS;
import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputPublish implements IInputPublish {

    @Override
    public void inputMessageWithDisk(String queName, String body,  int seq) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        int quemode=iMemoryOPS.getQueMode(queName);
        switch (quemode){
            case Mode.MODE_DIRECT:
                iMemoryOPS.inputDirectMessage(queName,body,seq);
                break;
            case Mode.MODE_FANOUT:
                iMemoryOPS.inputFanoutMessage(queName,body,seq);
                break;
            case Mode.MODE_TOPIC:
                iMemoryOPS.inputTopicMessage(queName,body,seq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
        //TODO
    }

    @Override
    public void inputMessageWithDisk(String queName, String body) throws Exception {
        inputMessageWithDisk(queName,body,0);
    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body,  int seq) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        int quemode=iMemoryOPS.getQueMode(queName);
        switch (quemode){
            case Mode.MODE_DIRECT:
                iMemoryOPS.inputDirectMessage(queName,body,seq);
                break;
            case Mode.MODE_FANOUT:
                iMemoryOPS.inputFanoutMessage(queName,body,seq);
                break;
            case Mode.MODE_TOPIC:
                iMemoryOPS.inputTopicMessage(queName,body,seq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body) throws Exception {
        inputMessageWithoutDisk(queName,body,0);
    }
}
