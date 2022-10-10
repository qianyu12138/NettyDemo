package com.example.handler;

import com.example.tran.packet.GroupMessageResponsePacket;
import com.example.tran.packet.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class GroupMessageResponseHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket groupMessageResponsePacket) throws Exception {
        if(groupMessageResponsePacket.getCode() != 0) {
            System.out.println(new Date() + " 发送消息失败:" + groupMessageResponsePacket.getMessage());
        }
    }
}
