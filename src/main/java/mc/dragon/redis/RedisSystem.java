package mc.dragon.redis;

import com.google.gson.Gson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import mc.dragon.redis.impl.GsonCodec;
import mc.dragon.redis.packet.Packet;

public class RedisSystem {

    private static final Gson GSON = new Gson();
    private static final GsonCodec PACKET_CODEC = new GsonCodec(GSON, Packet.class);

    private static RedisClient redisClient;
    private static StatefulRedisPubSubConnection<String, Packet> pubSubConnection;
    private static StatefulRedisConnection<String, String> databaseConnect;
    private static StatefulRedisConnection<String, Packet> connection;

    public void connect(String host, String password) {
        redisClient = RedisClient.create(RedisURI.builder()
                .withHost(host)
                .withPort(25584)
                .withPassword(password.toCharArray())
                .build());

        pubSubConnection = redisClient.connectPubSub(PACKET_CODEC);
        databaseConnect = redisClient.connect();
        connection = redisClient.connect(PACKET_CODEC);

        System.out.println("[RedisSystem] Connected to Redis!");
    }

    public void disconnect() {
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

    public static Gson getGson() {
        return GSON;
    }

    public static RedisClient getRedisClient() {
        return redisClient;
    }

    public static StatefulRedisConnection<String, Packet> getConnection() {
        return connection;
    }

    public static StatefulRedisConnection<String, String> getDatabaseConnect() {
        return databaseConnect;
    }

    public static StatefulRedisPubSubConnection<String, Packet> getPubSubConnection() {
        return pubSubConnection;
    }
}