package org.client.consumer;

import org.apache.log4j.Logger;
import org.client.consumer.intf.Consume;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.QueueConfig;


/**
 * Created by andilyliao on 16-4-7.
 */
public class QueueConsumer extends Consume implements IConsumer {
    private static Logger log = Logger.getLogger(QueueConsumer.class);
    private Config config;
    private QueueConfig queueConfig;
    public QueueConsumer(Config config) {
        this.config = config;
    }
    @Override
    public void initConsumer(Config config) throws Exception {

    }
}
