package org.client.consumer.util.result;

import org.httpqueue.protocolbean.Mode;

/**
 * Created by andilyliao on 16-4-7.
 */
public class MsgRes {
    private int isHasDate= Mode.DATA_NO;
    private long offset=0;
    private long putset=0;
    private int seq=0;
    private int totleseq=0;
    private String body="";

    public int getIsHasDate() {
        return isHasDate;
    }

    public void setIsHasDate(int isHasDate) {
        this.isHasDate = isHasDate;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public long getPutset() {
        return putset;
    }

    public void setPutset(long putset) {
        this.putset = putset;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getTotleseq() {
        return totleseq;
    }

    public void setTotleseq(int totleseq) {
        this.totleseq = totleseq;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
