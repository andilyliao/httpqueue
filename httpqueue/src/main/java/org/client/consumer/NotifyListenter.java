package org.client.consumer;

/**
 * Created by andilyliao on 16-4-2.
 */
import org.apache.log4j.Logger;
import redis.clients.jedis.JedisPubSub;

/**
 * 订阅监听类
 * Created by Wizzer on 14-4-8.
 */
public class NotifyListenter extends JedisPubSub {
    private static Logger log = Logger.getLogger(NotifyListenter.class);
    // 取得订阅的消息后的处理
    public void onMessage(String channel, String message) {
        log.debug(channel + "=" + message);
    }

    // 初始化订阅时候的处理
    public void onSubscribe(String channel, int subscribedChannels) {
        log.debug(channel + "=" + subscribedChannels);
    }

    // 取消订阅时候的处理
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.debug(channel + "=" + subscribedChannels);
    }

    // 初始化按表达式的方式订阅时候的处理
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.debug(pattern + "=" + subscribedChannels);
    }

    // 取消按表达式的方式订阅时候的处理
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        log.debug(pattern + "=" + subscribedChannels);
    }

    // 取得按表达式的方式订阅的消息后的处理
    public void onPMessage(String pattern, String channel, String message) {
        log.debug(pattern + "=" + channel + "=" + message);
    }
}