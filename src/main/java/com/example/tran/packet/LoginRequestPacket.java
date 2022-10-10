package com.example.tran.packet;

import com.example.tran.CommandEnum;
import com.example.tran.Packet;
import lombok.Data;

@Data
public class LoginRequestPacket extends Packet {

    private String userId;
    private String username;
    private String password;

    @Override
    public Byte getCommand() {
        return CommandEnum.LOGIN_REQUEST.getCode();
    }
}
