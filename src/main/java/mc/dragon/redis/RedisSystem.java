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

    /**
     * Połączenie z Redis.
     * Jeśli username == null/empty → używa tylko hasła (requirepass).
     * Jeśli username != null → używa ACL (username+password).
     */
    public void connect(String host, int port, String username, String password) {
        RedisURI.Builder builder = RedisURI.builder()
                .withHost(host)
                .withPort(port);

        if (username != null && !username.isEmpty()) {
            builder.withAuthentication(username, password == null ? new char[0] : password.toCharArray());
        } else if (password != null && !password.isEmpty()) {
            builder.withPassword(password.toCharArray());
        }

        redisClient = RedisClient.create(builder.build());

        pubSubConnection = redisClient.connectPubSub(PACKET_CODEC);
        databaseConnect = redisClient.connect();
        connection = redisClient.connect(PACKET_CODEC);

        System.out.printf("[RedisSystem] Connected to Redis at %s:%d (user=%s)%n",
                host, port, username != null && !username.isEmpty() ? username : "<default>");
    }

    public void disconnect() {
        if (redisClient != null) {
            redisClient.shutdown();
            System.out.println("[RedisSystem] Disconnected from Redis.");
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