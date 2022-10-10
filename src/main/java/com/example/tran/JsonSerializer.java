package com.example.tran;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

import java.nio.charset.StandardCharsets;

public class JsonSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object obj) {
        return JSONUtil.toJsonStr(obj).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSONUtil.toBean(new String(bytes, StandardCharsets.UTF_8), clazz);
    }
}
