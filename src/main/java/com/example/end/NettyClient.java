package com.example.end;

import com.example.handler.*;
import com.example.tran.PacketCodeC;
import com.example.tran.packet.GroupMessageRequestPacket;
import com.example.tran.packet.LoginRequestPacket;
import com.example.tran.packet.MessageRequestPacket;
import com.example.util.ConcurrentUtil;
import com.example.vo.ServerContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        ch.pipeline().addLast(new LoginResponseClientHandler());
                        ch.pipeline().addLast(new HeartBeatRequestHandler());
                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                        ch.pipeline().addLast(new MessageRequestHandler());
                        ch.pipeline().addLast(new MessageResponseHandler());
                        ch.pipeline().addLast(new GroupMessageRequestHandler());
                        ch.pipeline().addLast(new GroupMessageResponseHandler());
                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });


        connect(bootstrap, "127.0.0.1", 8000, MAX_RETRY);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("????????????!");
                ChannelFuture channelFuture = (ChannelFuture) future;
                startConsoleThread(channelFuture.channel());
            } else if (retry == 0) {
                System.err.println("???????????????????????????????????????");
            } else {
                // ???????????????
                int order = (MAX_RETRY - retry) + 1;
                // ?????????????????????
                int delay = 1 << order;
                System.err.println(new Date() + ": ??????????????????" + order + "???????????????");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (ServerContext.hasLogin(channel)) {
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();
                    if (line.startsWith("@")) {
                        if (!line.contains(":")) {
                            System.out.println("????????????");
                            continue;
                        }
                        String toUserId = line.substring(1, line.indexOf(":"));
                        String message = line.substring(line.indexOf(":") + 1);
                        MessageRequestPacket packet = new MessageRequestPacket();
                        packet.setMessage(message);
                        packet.setToUserId(toUserId);
                        ByteBuf buf = PacketCodeC.INSTANCE.encode(channel.alloc(), packet);
                        channel.writeAndFlush(buf);
                    } else {
                        GroupMessageRequestPacket messageRequestPacket = new GroupMessageRequestPacket();
                        messageRequestPacket.setMessage(line);
                        channel.writeAndFlush(messageRequestPacket);
                    }
                } else {
                    System.out.println("?????????:");
                    Scanner sc = new Scanner(System.in);
                    String userId = sc.nextLine();
                    System.out.println("??????:");
                    String pwd = sc.nextLine();
                    LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
                    loginRequestPacket.setUserId(userId);
                    loginRequestPacket.setPassword(pwd);
                    channel.writeAndFlush(loginRequestPacket);
                    ConcurrentUtil.waitIt();
                }
            }

        }).start();
    }
}