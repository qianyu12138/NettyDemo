package com.example.handler;

import com.example.tran.PacketCodeC;
import com.example.tran.packet.MessageRequestPacket;
import com.example.tran.packet.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        System.out.println(new Date() + " " + messageRequestPacket.getFromUserId() + "->YOU:" + messageRequestPacket.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setCode(0);
        messageResponsePacket.setMessage("receive message success");
        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
