package org.httpqueue.outprocess;

import org.httpqueue.outprocess.intf.IOutProcessor;
import org.httpqueue.outprocess.task.OutputConsume;
import org.httpqueue.outprocess.task.OutputRegist;
import org.httpqueue.outprocess.task.intf.IOutputConsume;
import org.httpqueue.outprocess.task.intf.IOutputRegist;
import org.httpqueue.protocolbean.MessageBody;
import org.httpqueue.protocolbean.Mode;
import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-3-31.
 */
public class OutProcessor implements IOutProcessor {
    @Override
    public MessageBody process(String clientID,String queueName, OutputHead outputHead) throws Exception {
        int type=outputHead.getTy();
        int hashdisk=outputHead.getH();
        MessageBody body=new MessageBody();
        switch(type){
            case Mode.OUTPUTTYPE_REGIST:
                IOutputRegist iOutputRegist=new OutputRegist();
                int mode=iOutputRegist.getQueMode(queueName);
                switch (mode) {
                    case Mode.MODE_DIRECT:
                        iOutputRegist.registDirect(queueName);
                        break;
                    case Mode.MODE_FANOUT:
                        iOutputRegist.registFanout(clientID,queueName);
                        break;
                    case Mode.MODE_TOPIC:
                        iOutputRegist.registTopic(clientID,queueName);
                        break;
                    default:
                        throw new Exception("This queue doesn't has right mode value,please check db");
                }
                break;
            case Mode.OUTPUTTYPE_CONSUME:
                int offset=outputHead.getO();
                int seq=outputHead.getS();
                IOutputConsume iOutputConsume=new OutputConsume();
                switch (hashdisk){
                    case Mode.HASHDISK_WITHDISK:
                        body=iOutputConsume.consumeMessageWithDisk(clientID,queueName,offset,seq);
                        break;
                    case Mode.HASHDISK_WITHOUTDISK:
                        body=iOutputConsume.consumeMessageWithoutDisk(clientID,queueName,offset,seq);
                        break;
                    default:
                        throw new Exception("Header doesn't has hashdisk param");
                }
                break;
            default:
                throw new Exception("Header doesn't has type param");
        }
        return body;
    }
}
