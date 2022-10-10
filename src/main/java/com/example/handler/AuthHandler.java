package com.example.handler;

import com.example.tran.packet.MessageRequestPacket;
import com.example.vo.Attributes;
import com.example.vo.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (ServerContext.hasLogin(ctx.channel())) {
            try {
                ctx.pipeline().remove(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.channelRead(ctx, msg);
        } else {
            ctx.channel().close();
        }
    }

}
