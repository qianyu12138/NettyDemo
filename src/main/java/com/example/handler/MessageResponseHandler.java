package com.example.handler;

import com.example.tran.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws Exception {
        if(messageResponsePacket.getCode() != 0) {
            System.out.println(new Date() + " 发送消息失败:" + messageResponsePacket.getMessage());
        }
    }
}
