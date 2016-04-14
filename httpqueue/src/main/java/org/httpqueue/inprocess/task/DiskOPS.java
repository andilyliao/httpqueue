package org.httpqueue.inprocess.task;

import org.httpqueue.inprocess.task.intf.IDiskOPS;

/**
 * Created by andilyliao on 16-3-31.
 */
public class DiskOPS implements IDiskOPS {
    @Override
    public void inputDirectMessage(String queName, String body, int seq, int totleseq) throws Exception {

    }

    @Override
    public void inputFanoutMessage(String queName, String body, int seq, int totleseq) throws Exception {

    }
}
