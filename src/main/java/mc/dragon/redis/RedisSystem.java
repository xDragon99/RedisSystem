package mc.dragon.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import mc.dragon.redis.impl.FSTCodec;
import mc.dragon.redis.packet.Packet;
import org.nustaq.serialization.FSTConfiguration;

public class RedisSystem {

    private static final FSTConfiguration FST_CONFIG = FSTConfiguration.createDefaultConfiguration();
    private static final FSTCodec FST_CODEC = new FSTCodec();

    private static RedisClient redisClient;
    private static StatefulRedisPubSubConnection<String, Packet> pubSubConnection;
    private static StatefulRedisConnection<String, String> databaseConnect;
    private static StatefulRedisConnection<String, Packet> connection;

    public void connect(String host, String password) {
        redisClient = RedisClient.create(RedisURI.builder()
                .withHost(host)
                .withPort(6379)
                .withPassword(password.toCharArray())
                .build());

        pubSubConnection = redisClient.connectPubSub(FST_CODEC);
        databaseConnect = redisClient.connect();
        connection = redisClient.connect(FST_CODEC);

        System.out.println("[RedisSystem] Connected to Redis!");
    }

    public void disconnect() {
        if (redisClient != null) {
            redisClient.shutdown();
        }
    }

    public static FSTConfiguration getFstConfig() {
        return FST_CONFIG;
    }

    public static FSTCodec getFstCodec() {
        return FST_CODEC;
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