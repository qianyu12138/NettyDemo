package com.example.vo;

import io.netty.channel.Channel;
import io.netty.util.Attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerContext {

    private static final Map<String, Channel> CHANNEL_MAP = new HashMap<>();

    public static void addSession(String userId, Channel channel) {
        CHANNEL_MAP.put(userId, channel);
    }

    public static void clearSession(String userId) {
        CHANNEL_MAP.remove(userId);
    }

    public static Channel getSession(String userId) {
        return CHANNEL_MAP.get(userId);
    }

    public static List<Channel> getAll(String exUserId) {
        List<Channel> all = new ArrayList<>();
        CHANNEL_MAP.forEach((key, value) -> {
            if (exUserId != null && exUserId.equals(key)) return;
            all.add(value);
        });
        return all;
    }

    public static void markAsLogin(Channel channel, String userId) {
        channel.attr(Attributes.LOGIN).set(userId);
    }
    public static String getLogin(Channel channel){
        return channel.attr(Attributes.LOGIN).get();
    }
    public static boolean hasLogin(Channel channel) {
        Attribute<String> attr = channel.attr(Attributes.LOGIN);
        return attr.get() != null;
    }

    public static Map<String,Channel> getMap(){
        return CHANNEL_MAP;
    }
}
