package com.example.handler;

import com.example.tran.packet.MessageRequestPacket;
import com.example.tran.packet.MessageResponsePacket;
import com.example.vo.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 服务端用的消息接收处理器，需要转发
 */
public class MessageRequestServerHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) throws Exception {
        //转发
        String toUserId = messageRequestPacket.getToUserId();
        String fromUserId = ServerContext.getLogin(ctx.channel());
        System.out.println(new Date() + " " + fromUserId + "->" + messageRequestPacket.getToUserId() + ":" + messageRequestPacket.getMessage());

        Channel toChannel = ServerContext.getSession(toUserId);
        if (toChannel == null) {
            //回复发送者失败结果
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setCode(-1);
            messageResponsePacket.setMessage("User is offline.");
            ctx.channel().writeAndFlush(messageResponsePacket);
        } else {
            //转发
            MessageRequestPacket messageRequestPacket1 = new MessageRequestPacket();
            messageRequestPacket1.setFromUserId(fromUserId);
            messageRequestPacket1.setToUserId(toUserId);
            messageRequestPacket1.setMessage(messageRequestPacket.getMessage());
            toChannel.writeAndFlush(messageRequestPacket1);

            //回复发送者成功结果
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("receive message success");
            messageResponsePacket.setCode(0);
            ctx.channel().writeAndFlush(messageResponsePacket);
        }

    }
}
