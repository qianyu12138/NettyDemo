package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;
import lombok.Data;

@Data
public class MessageRequestPacket extends Packet {
    private String message;
    private String toUserId;
    private String fromUserId;

    @Override
    public Byte getCommand() {
        return CommandEnum.MESSAGE_REQUEST.getCode();
    }
}
