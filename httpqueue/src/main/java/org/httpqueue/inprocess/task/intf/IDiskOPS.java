package org.httpqueue.inprocess.task.intf;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IDiskOPS {
    public void inputDirectMessage(String queName,String body,long pubset,int seq,int totleseq)throws Exception;
    public void inputFanoutMessage(String queName,String body,long pubset,int seq,int totleseq)throws Exception;
}
