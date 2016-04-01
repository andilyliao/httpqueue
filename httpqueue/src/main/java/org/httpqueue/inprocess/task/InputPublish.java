package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IInputPublish;

/**
 * Created by andilyliao on 16-4-1.
 */
public class InputPublish implements IInputPublish {

    @Override
    public void inputMessageWithDisk(String queName, String body, int ttl, int tr, int seq) throws Exception {

    }

    @Override
    public void inputMessageWithDisk(String queName, String body, int ttl) throws Exception {

    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body, int ttl, int tr, int seq) throws Exception {

    }

    @Override
    public void inputMessageWithoutDisk(String queName, String body, int ttl) throws Exception {

    }
}
