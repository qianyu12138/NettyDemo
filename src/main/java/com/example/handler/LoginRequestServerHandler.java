package com.example.handler;

import com.example.tran.packet.LoginRequestPacket;
import com.example.tran.packet.LoginResponsePacket;
import com.example.tran.packet.MessageRequestPacket;
import com.example.vo.Attributes;
import com.example.vo.ServerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;
import java.util.List;

public class LoginRequestServerHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket loginRequestPacket) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        if (valid(loginRequestPacket)) {
            validLoginStatus(loginRequestPacket.getUserId(), ctx.channel());
            loginResponsePacket.setCode(0);
            loginResponsePacket.setMsg("登录成功");
            ServerContext.markAsLogin(ctx.channel(),loginRequestPacket.getUserId());
            ServerContext.addSession(loginRequestPacket.getUserId(), ctx.channel());
            noticeOthers(loginRequestPacket.getUserId());
            System.out.println("用户[" + loginRequestPacket.getUserId() + "]已上线");
        } else {
            loginResponsePacket.setCode(-1);
            loginResponsePacket.setMsg("登录失败");
            System.out.println("连接失败");
        }
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    /**
     * 多端登录
     * @param userId
     */
    private void validLoginStatus(String userId, Channel newChannel) {
        Channel oldChannel = ServerContext.getSession(userId);
        if(oldChannel != null &&  ServerContext.hasLogin(oldChannel)){
            if(newChannel.id().equals(oldChannel.id())){
                //同一端
                return;
            } else {
                //新端
                MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                messageRequestPacket.setFromUserId("系统通知");
                messageRequestPacket.setMessage("您的账号已被迫下线");
                messageRequestPacket.setToUserId(userId);
                oldChannel.writeAndFlush(messageRequestPacket);
                oldChannel.pipeline().remove(LoginRequestServerHandler.class);//避免触发下线方法
                oldChannel.close();
                System.out.println(userId + "旧端下线");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(Attributes.CONNECT_TIME).set(LocalDateTime.now());
        super.channelActive(ctx);
    }

    private void noticeOthers(String userId) {
        List<Channel> others = ServerContext.getAll(userId);
        MessageRequestPacket requestPacket = new MessageRequestPacket();
        requestPacket.setFromUserId("系统通知");
        requestPacket.setMessage("用户[" + userId + "]已上线");
        for (Channel other : others) {
            other.writeAndFlush(requestPacket);
        }
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String offlineUserId = ctx.channel().attr(Attributes.LOGIN).get();
        ServerContext.clearSession(offlineUserId);
        System.out.println("用户[" + offlineUserId + "]已下线");
        //发送下线消息
        List<Channel> all = ServerContext.getAll(null);
        for (Channel channel : all) {
            MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
            messageRequestPacket.setFromUserId("系统通知");
            messageRequestPacket.setMessage("用户[" + offlineUserId + "]已下线");
            channel.writeAndFlush(messageRequestPacket);
        }
        super.channelInactive(ctx);
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
