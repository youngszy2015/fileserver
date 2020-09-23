package com.y;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class ServerCommandHandler extends ChannelDuplexHandler {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String ms = (String) msg;
        ByteBuf byteBuf = ctx.channel().alloc().buffer().writeBytes(ms.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int i = buf.readableBytes();
        System.err.println("server readableBytes： " + i);


        ctx.channel().writeAndFlush("server-response-01");
    }

}
