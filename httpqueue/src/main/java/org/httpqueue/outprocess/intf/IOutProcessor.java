package org.httpqueue.outprocess.intf;

import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IOutProcessor {
    public String process(String queueName,OutputHead outputHead) throws Exception;
}
