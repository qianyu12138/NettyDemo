package com.example.handler;

import com.example.tran.packet.LoginResponsePacket;
import com.example.util.ConcurrentUtil;
import com.example.vo.ServerContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class LoginResponseClientHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) throws Exception {
        if (loginResponsePacket.getCode() == 0) {
            System.out.println("登录成功");

            ConcurrentUtil.continueIt();
            ServerContext.markAsLogin(ctx.channel(),"true");
        } else {
            System.out.println("登录失败," + loginResponsePacket.getMsg());
        }
    }
}
