package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;
import lombok.Data;

@Data
public class MessageResponsePacket extends Packet {
    private Integer code;
    private String message;

    @Override
    public Byte getCommand() {
        return CommandEnum.MESSAGE_RESPONSE.getCode();
    }
}
