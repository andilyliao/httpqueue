package org.client.publisher.util.queueconfig;

/**
 * Created by andilyliao on 16-4-6.
 */
public class TopicQueueConfig {
    //"qn":"mydirectqueue"
    private String queueName="";

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
