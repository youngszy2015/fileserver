package com.y;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class ClientCommandHandler extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String ms = (String) msg;
        byte[] bytes = ms.getBytes(StandardCharsets.UTF_8);
        System.out.println("client write byte len: " + bytes.length);
        ByteBuf byteBuf = ctx.channel().alloc().buffer().writeBytes(bytes);
        ctx.writeAndFlush(byteBuf);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int i = buf.readableBytes();
        System.err.println("client readableBytes： " + i);
    }
}
