package com.example.vo;

import io.netty.util.AttributeKey;

import java.time.LocalDateTime;

public interface Attributes {
    AttributeKey<String> LOGIN = AttributeKey.newInstance("login");
    AttributeKey<LocalDateTime> CONNECT_TIME = AttributeKey.newInstance("connect_time");
}
