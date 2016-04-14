package org.client.consumer.intf;

import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.QueueConfig;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;

/**
 * Created by andilyliao on 16-4-2.
 */
public interface IConsumer {
    public void initConsumer(Config config)throws Exception;
    public MsgRes consumeMsg(MsgRes msgRes)throws Exception;
}
