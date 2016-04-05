package org.httpqueue.inprocess;

import org.httpqueue.inprocess.intf.IInProcessor;
import org.httpqueue.inprocess.task.InputPublish;
import org.httpqueue.inprocess.task.intf.IInputCreate;
import org.httpqueue.inprocess.task.InputCreate;
import org.httpqueue.inprocess.task.intf.IInputPublish;
import org.httpqueue.protocolbean.InputHead;
import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-3-31.
 */
//log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);

public class InProcessor implements IInProcessor {
    @Override
    public void process(String queueName,InputHead inputHead, String body) throws Exception {
        int type=inputHead.getTy();
        int hashdisk=inputHead.getH();
        switch (type){
            case Mode.INPUTTYPE_CREATEQUEUE:
                IInputCreate inputCreate=new InputCreate();
                int mode=inputHead.getM();
                int ttl=inputHead.getT();
                switch (mode){
                    case Mode.MODE_DIRECT:
                        switch (hashdisk){
                            case Mode.HASHDISK_WITHDISK:
                                inputCreate.createDirectWithDisk(queueName,ttl);
                                break;
                            case Mode.HASHDISK_WITHOUTDISK:
                                inputCreate.createDirectWithoutDisk(queueName,ttl);
                                break;
                            default:
                                throw new Exception("Header doesn't has hashdisk param");
                        }
                        break;
                    case Mode.MODE_FANOUT:
                        switch (hashdisk){
                            case Mode.HASHDISK_WITHDISK:
                                inputCreate.createFanoutWithDisk(queueName,ttl);
                                break;
                            case Mode.HASHDISK_WITHOUTDISK:
                                inputCreate.createFanoutWithoutDisk(queueName,ttl);
                                break;
                            default:
                                throw new Exception("Header doesn't has hashdisk param");
                        }
                        break;
                    case Mode.MODE_TOPIC:
                        inputCreate.createTopic(queueName);
                        break;
                    default:
                        throw new Exception("Header doesn't has mode param");
                }
                break;
            case Mode.INPUTTYPE_PUBLISH:
                IInputPublish inputPublish=new InputPublish();
                int hastransaction=inputHead.getTr();
                int seq=inputHead.getS();
                int totleseq=inputHead.getTs();
                switch (hashdisk){
                    case Mode.HASHDISK_WITHDISK:
                        switch (hastransaction){
                            case Mode.HASTRANSACTION_NOTRANSACTION:
                                inputPublish.inputMessageWithDisk(queueName,body);
                                break;
                            case Mode.HASTRANSACTION_TRANSACTION:
                                inputPublish.inputMessageWithDisk(queueName,body,seq,totleseq);
                                break;
                            default:
                                throw new Exception("Header doesn't has hastransaction param");
                        }
                        break;
                    case Mode.HASHDISK_WITHOUTDISK:
                        switch (hastransaction){
                            case Mode.HASTRANSACTION_NOTRANSACTION:
                                inputPublish.inputMessageWithoutDisk(queueName,body);
                                break;
                            case Mode.HASTRANSACTION_TRANSACTION:
                                inputPublish.inputMessageWithoutDisk(queueName,body,seq,totleseq);
                                break;
                            default:
                                throw new Exception("Header doesn't has hastransaction param");
                        }
                        break;
                    default:
                        throw new Exception("Header doesn't has hashdisk param");
                }
                break;
            default:
                throw new Exception("Header doesn't has type param");
        }
    }
}
