package org.httpqueue.inprocess;

import org.httpqueue.protocolbean.Head;

/**
 * Created by andilyliao on 16-3-31.
 */
public interface IInProcessor {
    public void process(Head head,String body) throws Exception;
}
