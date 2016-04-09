package org.client.consumer.util.config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by andilyliao on 16-4-9.
 */
public class Contral {
    private CountDownLatch countDownLatch=null;
    private AtomicInteger pingnum=null;



    public AtomicInteger getPingnum() {
        return pingnum;
    }

    public void setPingnum(AtomicInteger pingnum) {
        this.pingnum = pingnum;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
