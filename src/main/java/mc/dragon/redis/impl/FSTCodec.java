package mc.dragon.redis.impl;

import io.lettuce.core.codec.RedisCodec;
import mc.dragon.redis.RedisSystem;
import mc.dragon.redis.packet.Packet;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FSTCodec implements RedisCodec<String, Packet> {

    @Override
    public String decodeKey(ByteBuffer bytes) {
        return StandardCharsets.UTF_8.decode(bytes).toString();
    }

    @Override
    public Packet decodeValue(ByteBuffer bytes) {
        byte[] buffer = new byte[bytes.remaining()];
        bytes.get(buffer);
        return (Packet) RedisSystem.getFstConfig().asObject(buffer);
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return StandardCharsets.UTF_8.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(Packet value) {
        return ByteBuffer.wrap(RedisSystem.getFstConfig().asByteArray(value));
    }
}