package org.client.publisher;


import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.client.publisher.intf.IPublisher;
import org.client.publisher.intf.Publish;
import org.client.publisher.util.config.Config;
import org.client.publisher.util.messageconfig.Message;
import org.client.publisher.util.queueconfig.MemoryFanoutQueueConfig;
import org.client.publisher.util.result.CommonRes;

/**
 * Created by andilyliao on 16-4-2.
 */
//
public class MemoryFanoutQueuePublisher extends Publish implements IPublisher {
    private static Logger log = Logger.getLogger(MemoryFanoutQueuePublisher.class);
    private Config config;
    public MemoryFanoutQueuePublisher(Config config){
        this.config=config;
    }
    private MemoryFanoutQueueConfig queueConfig;
    @Override
    public void initPublisher(Config config) throws Exception {
        this.config=config;
    }
    //curl http://localhost:8844/queue -d '{"head":{"qn":"myfanoutqueue","ty":0,"m":1,"t":86400,"h":0}}'
    public CommonRes createFanoutQueue(MemoryFanoutQueueConfig queueConfig)throws Exception{
        this.queueConfig=queueConfig;
        String queueName = queueConfig.getQueueName();
        int ttl = queueConfig.getTtl();
        String url = this.config.urlMap.get(Math.abs(queueName.hashCode()) % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"ty\":0,\"m\":1,\"t\":"+ttl+",\"h\":0}}";
        String body = send(url, json, "utf-8");
        CommonRes cr= JSON.parseObject(body, CommonRes.class);
        return cr;
    }
//curl http://localhost:8844/queue -d '{"head":{"qn":"myfanoutqueue","ty":1,"tr":0,"s":0,"ts":0},"body":{"aaa":"bbb","ccc":"ddd"}}'
    @Override
    public CommonRes publishMessage(Message message) throws Exception {
        String queueName = this.queueConfig.getQueueName();
        String msg=message.getBody();
        String url = this.config.urlMap.get(Math.abs(queueName.hashCode()) % this.config.urlMap.size());
        String json = "{\"head\":{\"qn\":\""+queueName+"\",\"ty\":1,\"h\":0,\"tr\":0,\"s\":0,\"ts\":0},\"body\":\""+msg+"\"}";
        String body = send(url, json, "utf-8");
        CommonRes cr=JSON.parseObject(body, CommonRes.class);
        return cr;
    }
    public void initQueueWithoutCreate(MemoryFanoutQueueConfig queueConfig)throws Exception {
        this.queueConfig=queueConfig;
    }
}
