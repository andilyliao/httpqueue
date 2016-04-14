package org.client.publisher.util.queueconfig;

/**
 * Created by andilyliao on 16-4-6.
 */
public class PersistFanoutQueueConfig {
    //"qn":"mydirectqueue","t":86400
    private String queueName="";
    private int ttl=0;
    public static final int ONE_DAY=24*3600;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }
}
