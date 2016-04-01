package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-3-31.
 */
//log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);
public class Mode {
    //消息方式 0：1对1 1：1对多 2：订阅
    public final static int MODE_DIRECT=0;
    public final static int MODE_FANOUT=1;
    public final static int MODE_TOPIC=2;
    //是否持久化  0：不持久 1：持久（用在接收到，0：从内存取数据 1：从磁盘取数据）
    public final static int HASHDISK_WITHOUTDISK=0;
    public final static int HASHDISK_WITHDISK=1;
    //事务 0：无事务 1：有事务
    public final static int HASTRANSACTION_NOTRANSACTION=0;
    public final static int HASTRANSACTION_TRANSACTION=1;

    //发送方任务类型 0：创建队列 1：发送消息
    public final static int INPUTTYPE_CREATEQUEUE=0;
    public final static int INPUTTYPE_PUBLISH=1;
    //接收方任务类型 0：创建订阅队列 1：接收消息 2:注册监听队列
    public final static int OUTPUTTYPE_CREATETOPIC=0;
    public final static int OUTPUTTYPE_CONSUME=1;
    public final static int REGIST=2;
}
