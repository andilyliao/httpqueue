package org.client.publisher.util.messageconfig;

/**
 * Created by andilyliao on 16-4-6.
 */
//单条的消息
public class Message {
    //"body":{"aaa":"bbb","ccc":"ddd"}
    private String body="";

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
