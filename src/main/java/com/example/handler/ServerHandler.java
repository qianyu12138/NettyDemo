package com.example.handler;

import com.example.tran.packet.LoginRequestPacket;
import com.example.tran.Packet;
import com.example.tran.PacketCodeC;
import com.example.tran.packet.LoginResponsePacket;
import com.example.tran.packet.MessageRequestPacket;
import com.example.tran.packet.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(requestByteBuf);

        if(packet == null){
            return;
        }

        if(packet instanceof LoginRequestPacket){
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            if(valid(loginRequestPacket)){
                loginResponsePacket.setCode(0);
                loginResponsePacket.setMsg("登录成功");
                System.out.println("连接成功");
            } else {
                loginResponsePacket.setCode(-1);
                loginResponsePacket.setMsg("登录失败");
                System.out.println("连接失败");
            }
            ByteBuf resBytes = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(resBytes);
        } else if (packet instanceof MessageRequestPacket){
            // 处理消息
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
