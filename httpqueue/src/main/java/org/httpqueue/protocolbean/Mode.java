package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-3-31.
 */
public class Mode {
    //消息方式 0：1对1 1：1对多 2：订阅
    public final static int DIRECT=0;
    public final static int FANOUT=1;
    public final static int TOPIC=2;
    //是否持久化  0：不持久 1：持久
    public final static int WITHOUTDISK=0;
    public final static int WITHDISK=1;
    //事务 0：无事务 1：有事务
    public final static int NOTRANSACTION=0;
    public final static int TRANSACTION=1;

}
