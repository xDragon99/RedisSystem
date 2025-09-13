package mc.dragon.redis.utilities;

import mc.dragon.redis.RedisSystem;
import mc.dragon.redis.impl.RedisListener;
import mc.dragon.redis.packet.Packet;

import java.util.ArrayList;
import java.util.List;

public class RedisUtilities {


    private static final List<String> subscribedChannels = new ArrayList<>();
    protected static RedisUtilities instance;

    public static RedisUtilities getInstance() {
        if(instance == null)return new RedisUtilities();
        return instance;
    }
    public RedisUtilities(){
        instance = this;
    }

    public void publish(String channel, Packet packet){
        RedisSystem.getConnection().sync().publish(channel, packet);
    }
    public static void subscribe(String channel, RedisListener<? extends Packet> listener) {
        if (!channel.equals(listener.getChannel())) {
            throw new IllegalStateException("Channel from subscribe method must be the same as Listener channel");
        }

        RedisSystem.getPubSubConnection().addListener(listener);

        if (!subscribedChannels.contains(channel)) {
            RedisSystem.getPubSubConnection().sync().subscribe(channel);
            subscribedChannels.add(channel);
        }
    }
}
