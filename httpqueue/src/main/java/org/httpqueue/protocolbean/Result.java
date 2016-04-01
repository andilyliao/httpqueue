package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-4-1.
 */
public class Result {
    //错误码
    private int code;
    //返回body
    private String body;
    //返回状态
    private int status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
