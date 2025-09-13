package mc.dragon.redis.impl;

import com.google.gson.Gson;
import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class GsonCodec<T> implements RedisCodec<String, T> {

    private final Gson gson;
    private final Class<T> type;

    public GsonCodec(Gson gson, Class<T> type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return StandardCharsets.UTF_8.decode(bytes).toString();
    }

    @Override
    public T decodeValue(ByteBuffer bytes) {
        byte[] buffer = new byte[bytes.remaining()];
        bytes.get(buffer);
        String json = new String(buffer, StandardCharsets.UTF_8);
        return gson.fromJson(json, type);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return StandardCharsets.UTF_8.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(T value) {
        String json = gson.toJson(value);
        return StandardCharsets.UTF_8.encode(json);
    }
}