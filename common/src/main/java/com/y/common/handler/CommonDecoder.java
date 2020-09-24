package com.y.common.handler;

import com.y.common.codec.CodecManager;
import com.y.common.command.Command;
import com.y.common.command.CommandType;
import com.y.common.command.upload.UploadRequestCommand;
import com.y.common.command.upload.UploadResponseCommand;
import com.y.common.exception.CommonException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class CommonDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 8) {
            in.markReaderIndex();
            long totalLen = in.readLong();
            int i = in.readableBytes();
            if (i + 8 < totalLen) {
                in.resetReaderIndex();
            } else {
                byte[] bytes = new byte[(int) totalLen - 8];
                in.readBytes(bytes);
                Object command = doDecode(bytes);
                out.add(command);
            }
        }
    }

    /**
     * 完整的一个Command
     *
     * @param bytes
     */
    private Object doDecode(byte[] bytes) {
        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(bytes.length);
        buffer.writeBytes(bytes);
        buffer.markReaderIndex();
        short type = buffer.readShort();
        buffer.resetReaderIndex();
        try {
            return doDecodeCommand(type, bytes);

        } catch (CommonException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Object doDecodeCommand(short type, byte[] bytes) throws CommonException {
        com.y.common.codec.CommandDecoder decoder = CodecManager.getDecoderByCommandType(CommandType.valueOf(type));
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
		