package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;
import lombok.Data;

@Data
public class GroupMessageRequestPacket extends Packet {
    private String fromUserId;
    private String message;

    @Override
    public Byte getCommand() {
        return CommandEnum.GROUP_MESSAGE_REQUEST.getCode();
    }
}
