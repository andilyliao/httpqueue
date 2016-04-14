package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IDiskOPS;
import org.httpqueue.inprocess.task.intf.IInputPublish;
import org.httpqueue.inprocess.task.intf.IMemoryOPS;
import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputPublish implements IInputPublish {

    @Override
    public void inputMessageWithDisk(String queName, String body,  int seq,int totleseq) throws Exception {
        inputMessageWithoutDisk(queName,body,seq,totleseq);
        IDiskOPS iDiskOPS=new DiskOPS();
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        int quemode=iMemoryOPS.getQueMode(queName);
        long pubset=0;
        switch (quemode){
            case Mode.MODE_DIRECT:
                pubset=iMemoryOPS.inputDirectMessage(queName,body,seq,totleseq);
                iDiskOPS.inputDirectMessage(queName,body,pubset,seq,totleseq);
                break;
            case Mode.MODE_FANOUT:
                pubset=iMemoryOPS.inputFanoutMessage(queName,body,seq,totleseq);
                iDiskOPS.inputFanoutMessage(queName,body,pubset,seq,totleseq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
    }

    @Override
    public void inputMessageWithDisk(String queName, String body) throws Exception {
        inputMessageWithDisk(queName,body,0,0);
    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body,  int seq,int totleseq) throws Exception {
        IMemoryOPS iMemoryOPS=new MemoryOPS();
        int quemode=iMemoryOPS.getQueMode(queName);
        switch (quemode){
            case Mode.MODE_DIRECT: iMemoryOPS.inputFanoutMessage(queName,body,seq,totleseq);
                iMemoryOPS.inputDirectMessage(queName,body,seq,totleseq);
                break;
            case Mode.MODE_FANOUT:
                iMemoryOPS.inputFanoutMessage(queName,body,seq,totleseq);
                break;
            case Mode.MODE_TOPIC:
                iMemoryOPS.inputTopicMessage(queName,body,seq,totleseq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body) throws Exception {
        inputMessageWithoutDisk(queName,body,0,0);
    }
}
