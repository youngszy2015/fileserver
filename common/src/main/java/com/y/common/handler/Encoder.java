package com.y.common.handler;

import com.y.common.codec.CommandEncoder;
import com.y.common.command.Command;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Young
 * @DATE: 2020/9/22 20:56
 */
@ChannelHandler.Sharable
public class Encoder extends ChannelOutboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(Encoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof Command) {
            Command cmd = (Command) msg;
            CommandEncoder commandEncoder = cmd.getCommandEncoder();
            byte[] bytes = commandEncoder.encoder(cmd);
            ByteBuf byteBuf = ctx.channel().alloc().buffer().writeBytes(bytes);
            ChannelFuture channelFuture = ctx.channel().writeAndFlush(byteBuf);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("Encode command success:{} ", cmd);
                    } else {
                        future.cause().printStackTrace();
                    }
                }
            });
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
