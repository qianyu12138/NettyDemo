package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;

public class HeartBeatRequestPacket extends Packet {
    @Override
    public Byte getCommand() {
        return CommandEnum.HEARTBEAT_REQUEST.getCode();
    }
}
