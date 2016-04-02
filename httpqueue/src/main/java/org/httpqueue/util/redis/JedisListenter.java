package org.httpqueue.util.redis;


import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by andilyliao on 16-4-2.
 */
public class JedisListenter extends JedisPubSub {
    private static Logger log = Logger.getLogger(JedisListenter.class);
    // 取得订阅的消息后的处理
    public void onMessage(String channel, String message) {
        log.info(channel + "=" + message);
    }

    // 初始化订阅时候的处理
    public void onSubscribe(String channel, int subscribedChannels) {
        log.info(channel + "=" + subscribedChannels);
    }

    // 取消订阅时候的处理
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.info(channel + "=" + subscribedChannels);
    }

    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info(pattern + "=" + subscribedChannels);
    }

    // 取消按表达式的方式订阅时候的处理
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        log.info(pattern + "=" + subscribedChannels);
    }

    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
        log.info(pattern + "=" + channel + "=" + message);
    }
}