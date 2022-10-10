package com.example.handler;

import com.example.tran.packet.GroupMessageRequestPacket;
import com.example.tran.packet.GroupMessageResponsePacket;
import com.example.tran.packet.MessageRequestPacket;
import com.example.tran.packet.MessageResponsePacket;
import com.example.vo.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.List;

public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {
        System.out.println(new Date() + " " + groupMessageRequestPacket.getFromUserId() + ":" + groupMessageRequestPacket.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setCode(0);
        messageResponsePacket.setMessage("receive message success");
        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
