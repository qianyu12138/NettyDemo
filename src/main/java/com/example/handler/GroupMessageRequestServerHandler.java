package com.example.handler;

import com.example.tran.packet.GroupMessageRequestPacket;
import com.example.tran.packet.GroupMessageResponsePacket;
import com.example.vo.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.List;

public class GroupMessageRequestServerHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket groupMessageRequestPacket) throws Exception {

        //转发
        String fromUserId = ServerContext.getLogin(ctx.channel());

        System.out.println(Thread.currentThread().getName());
        System.out.println(new Date() + " " + fromUserId + ":" + groupMessageRequestPacket.getMessage());


        List<Channel> toChannels = ServerContext.getAll(fromUserId);
        for (Channel toChannel : toChannels) {
            //转发
            GroupMessageRequestPacket messageRequestPacket1 = new GroupMessageRequestPacket();
            messageRequestPacket1.setFromUserId(fromUserId);
            messageRequestPacket1.setMessage(groupMessageRequestPacket.getMessage());
            toChannel.writeAndFlush(messageRequestPacket1);
        }
        //回复发送者成功结果
        GroupMessageResponsePacket groupMessageResponsePacket = new GroupMessageResponsePacket();
        groupMessageResponsePacket.setMessage("receive message success");
        groupMessageResponsePacket.setCode(0);
        ctx.channel().writeAndFlush(groupMessageResponsePacket);
    }
}
