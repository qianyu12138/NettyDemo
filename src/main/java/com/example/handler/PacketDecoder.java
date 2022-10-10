package com.example.handler;

import com.example.tran.PacketCodeC;
import com.example.vo.ServerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
import java.util.Map;

public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        Map<String, Channel> map = ServerContext.getMap();
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
