package org.httpqueue.protocolbean;

/**
 * Created by andilyliao on 16-3-31.
 */
//{head:{m:0,t:100,d:0,tr:0,s:0}
public class InputHead  {
    //队列名
    private String qn="testQueue";
    //任务类型
    private int ty;
    //消息方式
    private int m;
    //消息缓存驻留时间
    private int t;
    //是否持久化
    private int h;
    //是否有事务
    private int tr;
    //事务中数据序号
    private int s;
    //事务中数据的总数
    private int ts;


    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }

    public int getTs() {
        return ts;
    }

    public void setTs(int ts) {
        this.ts = ts;
    }

    public String getQn() {
        return qn;
    }

    public void setQn(String qn) {
        this.qn = qn;
    }
}
