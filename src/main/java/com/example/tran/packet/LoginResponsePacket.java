package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;
import lombok.Data;

@Data
public class LoginResponsePacket extends Packet {

    private int code;
    private String msg;

    @Override
    public Byte getCommand() {
        return CommandEnum.LOGIN_RESPONSE.getCode();
    }
}
