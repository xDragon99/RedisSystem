package mc.dragon.redis.packet;

import mc.dragon.redis.utilities.RedisUtilities;

import java.io.Serial;
import java.io.Serializable;

public class Packet implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public void send(String channel) {
        RedisUtilities.getInstance().publish(channel, this);
    }
}