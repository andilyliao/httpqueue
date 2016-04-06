package org.testpublish;

import org.client.publisher.MemoryDirectQueuePublisher;
import org.client.publisher.intf.IPublisher;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryDirectQueueConfig;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by andilyliao on 16-4-2.
 */
public class PublishTest {
    @Test
    public void testpub() throws Exception {
        Config config=new Config("/publisher.properties");
        MemoryDirectQueuePublisher publisher=new MemoryDirectQueuePublisher(config);
        MemoryDirectQueueConfig memoryDirectQueueConfig=new MemoryDirectQueueConfig();
        memoryDirectQueueConfig.setQueueName("myque");
        memoryDirectQueueConfig.setTtl(memoryDirectQueueConfig.ONE_DAY);
        publisher.createDirectQueue(memoryDirectQueueConfig);
        Message message=new Message();
        message.setBody("test111");
        publisher.publishMessage(message);

    }
}
