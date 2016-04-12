package org.testpublish;

import org.client.publisher.MemoryDirectQueuePublisher;
import org.client.publisher.MemoryFanoutQueuePublisher;
import org.client.publisher.TopicQueuePublisher;
import org.client.publisher.intf.IPublisher;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryDirectQueueConfig;
import org.client.publisher.util.queueconfig.MemoryFanoutQueueConfig;
import org.client.publisher.util.queueconfig.TopicQueueConfig;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by andilyliao on 16-4-2.
 */
public class PublishTest {
    @Test
    public void testdirectmpub() throws Exception {
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
    @Test
    public void testdirectmpubdata()throws Exception{
        Config config=new Config("/publisher.properties");
        MemoryDirectQueuePublisher publisher=new MemoryDirectQueuePublisher(config);
        MemoryDirectQueueConfig memoryDirectQueueConfig=new MemoryDirectQueueConfig();
        memoryDirectQueueConfig.setQueueName("myque");
        memoryDirectQueueConfig.setTtl(memoryDirectQueueConfig.ONE_DAY);
        publisher.initQueueWithoutCreate(memoryDirectQueueConfig);
        Message message=new Message();
        message.setBody("cccc");
        publisher.publishMessage(message);
    }

    @Test
    public void testfanoutmpub() throws Exception {
        Config config=new Config("/publisher.properties");
        MemoryFanoutQueuePublisher publisher=new MemoryFanoutQueuePublisher(config);
        MemoryFanoutQueueConfig memoryFanoutQueueConfig=new MemoryFanoutQueueConfig();
        memoryFanoutQueueConfig.setQueueName("myque1");
        memoryFanoutQueueConfig.setTtl(memoryFanoutQueueConfig.ONE_DAY);
        publisher.createFanoutQueue(memoryFanoutQueueConfig);
        Message message=new Message();
        message.setBody("test111");
        publisher.publishMessage(message);

    }
    @Test
    public void testfanoutmpubdata()throws Exception{
        Config config=new Config("/publisher.properties");
        MemoryFanoutQueuePublisher publisher=new MemoryFanoutQueuePublisher(config);
        MemoryFanoutQueueConfig memoryFanoutQueueConfig=new MemoryFanoutQueueConfig();
        memoryFanoutQueueConfig.setQueueName("myque1");
        memoryFanoutQueueConfig.setTtl(memoryFanoutQueueConfig.ONE_DAY);
        publisher.initQueueWithoutCreate(memoryFanoutQueueConfig);
        Message message=new Message();
        message.setBody("cccc");
        publisher.publishMessage(message);
    }
    @Test
    public void testtopicmpub() throws Exception {
        Config config=new Config("/publisher.properties");
        TopicQueuePublisher publisher=new TopicQueuePublisher(config);
        TopicQueueConfig topicQueueConfig=new TopicQueueConfig();
        topicQueueConfig.setQueueName("myque2");
        publisher.createTopicQueue(topicQueueConfig);
        Message message=new Message();
        message.setBody("test111");
        publisher.publishMessage(message);

    }
    @Test
    public void testtopicmpubdata()throws Exception{
        Config config=new Config("/publisher.properties");
        TopicQueuePublisher publisher=new TopicQueuePublisher(config);
        TopicQueueConfig topicQueueConfig=new TopicQueueConfig();
        topicQueueConfig.setQueueName("myque2");
        publisher.initQueueWithoutCreate(topicQueueConfig);
        Message message=new Message();
        message.setBody("bbbb");
        publisher.publishMessage(message);
    }
}
