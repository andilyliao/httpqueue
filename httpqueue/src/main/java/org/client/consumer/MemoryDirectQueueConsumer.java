package org.client.consumer;

import org.apache.log4j.Logger;
import org.client.consumer.intf.Consume;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.MemoryDirectQueueConfig;
import org.client.publisher.MemoryDirectQueuePublisher;


/**
 * Created by andilyliao on 16-4-7.
 */
public class MemoryDirectQueueConsumer extends Consume implements IConsumer {
    private static Logger log = Logger.getLogger(MemoryDirectQueuePublisher.class);
    private Config config;
    private MemoryDirectQueueConfig queueConfig;
    public MemoryDirectQueueConsumer(Config config) {
        this.config = config;
    }
    @Override
    public void initConsumer(Config config) throws Exception {

    }
}
