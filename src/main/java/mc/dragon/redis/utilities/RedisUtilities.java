package mc.dragon.redis.utilities;

import mc.dragon.redis.RedisSystem;
import mc.dragon.redis.impl.RedisListener;
import mc.dragon.redis.packet.Packet;

import java.util.HashSet;
import java.util.Set;

public class RedisUtilities {

    private static final Set<String> subscribedChannels = new HashSet<>();
    private static RedisUtilities instance;

    public static RedisUtilities getInstance() {
        if (instance == null) {
            instance = new RedisUtilities();
        }
        return instance;
    }

    private RedisUtilities() {
        instance = this;
    }

    public void publish(String channel, Packet packet) {
        RedisSystem.getConnection().sync().publish(channel, packet);
    }

    public static void subscribe(String channel, RedisListener<? extends Packet> listener) {
        if (!channel.equals(listener.getChannel())) {
            throw new IllegalStateException("Channel mismatch: listener = " +
                    listener.getChannel() + ", subscribe() = " + channel);
        }

        RedisSystem.getPubSubConnection().addListener(listener);

        if (subscribedChannels.add(channel)) {
            RedisSystem.getPubSubConnection().sync().subscribe(channel);
        }
    }
}