package org.httpqueue.outprocess.task;

import org.httpqueue.inprocess.task.MemoryOPS;
import org.httpqueue.inprocess.task.intf.IMemoryOPS;
import org.httpqueue.outprocess.task.intf.IOutputConsume;
import org.httpqueue.outprocess.task.intf.IReadDisk;
import org.httpqueue.outprocess.task.intf.IReadMemory;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-4-1.
 */
public class OutputConsume implements IOutputConsume {
    @Override
    public MessageBody consumeMessageWithDisk(String clientID,String queName, int offset, int seq) throws Exception {
        MessageBody messageBody=new MessageBody();
        IReadMemory iReadMemory=new ReadMemory();
        IReadDisk iReadDisk=new ReadDisk();
        int quemode=iReadMemory.getQueMode(queName);
        switch (quemode){
            case Mode.MODE_DIRECT:
                messageBody=iReadDisk.outputDirect(queName,offset,seq);
                break;
            case Mode.MODE_FANOUT:
                messageBody=iReadDisk.outputFanout(clientID,queName,offset,seq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
        return messageBody;
    }

    @Override
    public MessageBody consumeMessageWithoutDisk(String clientID,String queName, int offset, int seq) throws Exception {
        MessageBody messageBody=new MessageBody();
        IReadMemory iReadMemory=new ReadMemory();
        int quemode=iReadMemory.getQueMode(queName);
        switch (quemode){
            case Mode.MODE_DIRECT:
                messageBody=iReadMemory.outputDirect(queName,offset,seq);
                break;
            case Mode.MODE_FANOUT:
                messageBody=iReadMemory.outputFanout(clientID,queName,offset,seq);
                break;
            case Mode.MODE_TOPIC:
                messageBody=iReadMemory.outputTopic(clientID,queName,offset,seq);
                break;
            default:
                throw new Exception("Que: "+queName+" doesn't have mode param!");
        }
        return messageBody;
    }
}
