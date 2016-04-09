package org.client.consumer.util.result;

/**
 * Created by andilyliao on 16-4-7.
 */
public class CommonRes {
    private int code=0;
    private int status=0;
    private String body="";

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
