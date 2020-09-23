package com.y.common.handler;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.Command;
import com.y.common.command.CommandType;
import com.y.common.command.upload.UploadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommandHandler.class);
    private boolean server;

    public CommandHandler(boolean server) {
        this.server = server;
    }

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
                        System.out.println("write success");
                    } else {
                        future.cause().printStackTrace();
                    }
                }
            });
        } else {
            super.write(ctx, msg, promise);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        int readableBytes = buf.readableBytes();
        if (readableBytes < 4) {
            return;
        }
        int totalBytes = buf.readInt();
        int remainBytes = buf.readableBytes();
        if (remainBytes < totalBytes) {
            return;
        }
        short type = buf.readShort();
        CommandDecoder decoder = CodecManager.getDecoderByCommandType(CommandType.valueOf(type));
        byte[] content = new byte[remainBytes - 2];
        buf.writeBytes(content);
        Object cmd = decoder.decoder(content);
        logger.info(cmd.toString());
        if (server) {
            //服务端模拟处理后写回响应
            UploadResponseCommand uploadResponseCommand = new UploadResponseCommand(0, "xxxxxxxxxxxxxx1");
            ctx.channel().writeAndFlush(uploadResponseCommand).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    System.out.println("response write success");
                } else {
                    System.err.println("response write error");

                }
            });
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel active: " + ctx.channel().id().asShortText());
        super.channelActive(ctx);
    }


}
