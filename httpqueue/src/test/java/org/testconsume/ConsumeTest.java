package org.testconsume;

import org.client.consumer.QueueConsumer;
import org.client.consumer.intf.IConsumer;
import org.client.consumer.util.config.Config;
import org.client.consumer.util.queueconfig.QueueConfig;
import org.client.consumer.util.result.CommonRes;
import org.client.consumer.util.result.MsgRes;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by andilyliao on 16-4-2.
 */
public class ConsumeTest {
    @Test
    public void testconsume() throws Exception {
        Config config=new Config("/consumer.properties");
        IConsumer iConsumer=new QueueConsumer(config);
        QueueConfig queueConfig=new QueueConfig();
        queueConfig.setQueueName("myque2");
        queueConfig.setUid(UUID.randomUUID().toString());
        System.out.println("start...........");
        CommonRes commonRes = iConsumer.registConsumer(queueConfig);
        MsgRes msgRes=new MsgRes();
        while(true) {
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println("========================: "+msgRes.getOffset());
            msgRes = iConsumer.consumeMsg(msgRes);//消费一条
//            System.out.println("========================: "+msgRes.getOffset());
//            System.out.println("------------------------: "+msgRes.getBody());
//            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            if (msgRes.getBody()!=null&&!msgRes.getBody().equals("")){
                System.out.println("------------------------: "+msgRes.getBody());
            }
        }
    }
}
