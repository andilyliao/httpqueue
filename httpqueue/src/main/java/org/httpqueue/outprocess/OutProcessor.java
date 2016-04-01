package org.httpqueue.outprocess;

import org.httpqueue.outprocess.intf.IOutProcessor;
import org.httpqueue.protocolbean.OutputHead;

/**
 * Created by andilyliao on 16-3-31.
 */
public class OutProcessor implements IOutProcessor {
    @Override
    public String process(String queueName, OutputHead outputHead) throws Exception {

        return "";
    }
}
