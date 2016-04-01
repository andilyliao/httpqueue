package org.httpqueue.inprocess.task.intf;

/**
 * Created by andilyliao on 16-4-1.
 */
//log.debug("Type is "+type+"mode is: " + mode + " ttl is: " + ttl + " hashdisk is: " + hashdisk + " hastransaction is: "+hastransaction+" seq is: " + seq);
public interface IInputPublish {
    public void inputMessageWithDisk(String queName,String body,int ttl,int seq)throws Exception;
    public void inputMessageWithDisk(String queName,String body,int ttl)throws Exception;
    public void inputMessageWithoutDisk(String queName,String body,int ttl,int seq)throws Exception;
    public void inputMessageWithoutDisk(String queName,String body,int ttl)throws Exception;
}
