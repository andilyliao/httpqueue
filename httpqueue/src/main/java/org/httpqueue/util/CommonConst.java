package org.httpqueue.util;

/**
 * Created by andilyliao on 16-4-2.
 */
public class CommonConst {
    public static final String splitor="!=end=!";//redis的key的华丽的分隔符
    public static final String PUBSET="PUBSET";//pubset前缀
    public static final String OFFSET="OFFSET";//offset前缀
    public static final String RECIVE="RECIVE";//消费者是否还在持续获取数据
    public static final String NOTIFY="HI";//用作通知的字符串
    //queue模式字符串字面量
    public static final String TYPE="TYPE";//Mode中queue模式作为值

    /**
     * 通过pubset或者offset以及事务seq，获取当前记录位置
     * @param puboffset
     * @param seq
     * @return
     */
    public static long puboffsetAndSeq(long puboffset,int seq){
        return puboffset*100000+seq;
    }

}
