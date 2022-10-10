package com.example.tran;

import com.example.tran.packet.*;

public enum CommandEnum {

    LOGIN_REQUEST((byte) 1, LoginRequestPacket.class),
    LOGIN_RESPONSE((byte) 11, LoginResponsePacket.class),
    MESSAGE_REQUEST((byte) 2, MessageRequestPacket.class),
    MESSAGE_RESPONSE((byte) 12, MessageResponsePacket.class),
    GROUP_MESSAGE_REQUEST((byte) 3, GroupMessageRequestPacket.class),
    GROUP_MESSAGE_RESPONSE((byte) 13, GroupMessageResponsePacket.class),
    HEARTBEAT_REQUEST((byte) 4, HeartBeatRequestPacket.class);

    private byte code;
    private Class<? extends Packet> clazz;

    CommandEnum(byte code, Class<? extends Packet> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public byte getCode() {
        return code;
    }

    public Class<? extends Packet> getClazz() {
        return clazz;
    }

    public static CommandEnum getByCode(byte code) {
        for (CommandEnum commandEnum : CommandEnum.values()) {
            if (code == commandEnum.getCode()) {
                return commandEnum;
            }
        }
        return null;
    }
}
