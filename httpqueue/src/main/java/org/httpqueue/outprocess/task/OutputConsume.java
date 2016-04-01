package org.httpqueue.outprocess.task;

import org.httpqueue.outprocess.task.intf.IOutputConsume;

/**
 * Created by andilyliao on 16-4-1.
 */
public class OutputConsume implements IOutputConsume {
    @Override
    public String consumeMessageWithDisk(String queName, int offset, int seq) throws Exception {
        return "";
    }

    @Override
    public String consumeMessageWithoutDisk(String queName, int offset, int seq) throws Exception {
        return "";
    }
}
