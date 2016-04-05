package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-4-2.
 */
public class MessageBody {
    private long offset=0;
    private long putset=0;
    private String body="";
    public MessageBody(){

    }
    public MessageBody(long putset,long offset,String body){
        this.offset=offset;
        this.body=body;
    }
    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getPutset() {
        return putset;
    }

    public void setPutset(long putset) {
        this.putset = putset;
    }
}
