package mc.dragon.redis.packet;

import mc.dragon.redis.utilities.RedisUtilities;

import java.io.Serializable;

public class Packet implements Serializable {


    public void send(String channel){
        RedisUtilities.getInstance().publish(channel, this);
    }
}
