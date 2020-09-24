package com.y.common.handler;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.command.CommandType;
import com.y.common.command.upload.UploadRequestCommand;
import com.y.common.command.upload.UploadResponseCommand;
import com.y.common.exception.CommonException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * @Author: Young
 * @DATE: 2020/9/22 20:57
 */
@Deprecated
public class Decoder extends ChannelInboundHandlerAdapter {

    private boolean first = true;

    private long totalBytes = 0;

    private ByteBuf cache;

    private long remainBytes = 0L;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        if (byteBuf.readableBytes() < 8) {
            return;
        }
        if (totalBytes == 0 && first) {
            totalBytes = byteBuf.readLong();
            if (totalBytes <= 0) {
                return;
            }
            remainBytes = totalBytes - 8;
            first = false;
            if (remainBytes <= byteBuf.readableBytes()) {
                cache = ctx.alloc().buffer((int) remainBytes);
            }

        }
        int crb = byteBuf.readableBytes();
        if (remainBytes - crb > 0) {
            //没读取完,缓存本次buf，设置新的remainBytes
            cache = ctx.alloc().buffer((int) (remainBytes));
            cache.writeBytes(byteBuf);
            remainBytes = remainBytes - crb;
            return;
        } else if (remainBytes - crb <= 0) {
            //读取完毕
            cache.writeBytes(byteBuf);
            cache.markReaderIndex();
            short type = cache.readShort();
            byte[] bytes = new byte[(int) (totalBytes - 8)];
            cache.resetReaderIndex();
            cache.readBytes(bytes);
            Object o = doDecoder(type, bytes);
            cache.release();
            ctx.fireChannelRead(o);
        } else {
            //下次读取
            totalBytes = 0;
            remainBytes = 0;
            first = true;
        }


    }

    private Object doDecoder(short type, byte[] bytes) throws CommonException {
        CommandDecoder decoder = CodecManager.getDecoderByCommandType(CommandType.valueOf(type));
        CommandType commandType = CommandType.valueOf(type);
        switch (commandType) {
            case UPLOAD_REQUEST:
                return (UploadRequestCommand) decoder.decoder(bytes);
            case UPLOAD_RESPONSE:
                return (UploadResponseCommand) decoder.decoder(bytes);
            case DOWNLOAD_REQUEST:
        }
        return decoder.decoder(bytes);
    }
}
