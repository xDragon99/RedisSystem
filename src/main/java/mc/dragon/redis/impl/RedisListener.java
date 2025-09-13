package mc.dragon.redis.impl;

import io.lettuce.core.pubsub.RedisPubSubListener;
import lombok.Getter;
import mc.dragon.redis.packet.Packet;

@Getter
public abstract class RedisListener<T extends Packet> implements RedisPubSubListener<String, Packet> {

    private final Class<T> packetType;
    private final String channel;

    public RedisListener(String channel, Class<T> packetType) {
        this.channel = channel;
        this.packetType = packetType;
    }

    public abstract void onMessage(T packet);

    @Override
    public void message(String channel, Packet packet) {
        if (this.channel.equalsIgnoreCase(channel)
                && packetType.isAssignableFrom(packet.getClass())) {
            @SuppressWarnings("unchecked")
            T casted = (T) packet;
            this.onMessage(casted);
        }
    }

    @Override
    public void message(String pattern, String channel, Packet message) {
    }

    @Override
    public void subscribed(String channel, long count) {
        System.out.printf("[RedisSystem] Subscribed to channel: %s (listener for %s)%n",
                channel, packetType.getSimpleName());
    }

    @Override
    public void psubscribed(String pattern, long count) {
    }

    @Override
    public void unsubscribed(String channel, long count) {
    }

    @Override
    public void punsubscribed(String pattern, long count) {
    }
}