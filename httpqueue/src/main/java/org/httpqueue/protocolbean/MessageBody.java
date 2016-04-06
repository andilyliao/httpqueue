package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-4-2.
 */
public class MessageBody {
    //队列中是否有数据
    private int isHasDate=0;
    private long offset=0;
    private long putset=0;
    private int seq=0;
    private int totleseq=0;
    private String body="";
    public MessageBody(){

    }
    public MessageBody(int isHasDate,long putset,long offset,String body,int seq,int totleseq){
        this.isHasDate=isHasDate;
        this.putset=putset;
        this.offset=offset;
        this.body=body;
        this.seq=seq;
        this.totleseq=totleseq;

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

    public int getTotleseq() {
        return totleseq;
    }

    public void setTotleseq(int totleseq) {
        this.totleseq = totleseq;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getIsHasDate() {
        return isHasDate;
    }

    public void setIsHasDate(int isHasDate) {
        this.isHasDate = isHasDate;
    }
}
