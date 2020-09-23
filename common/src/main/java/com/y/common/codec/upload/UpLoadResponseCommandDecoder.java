package com.y.common.codec.upload;

import com.y.common.codec.CommandDecoder;
import com.y.common.command.upload.UploadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Young
 * @DATE: 2020/9/21 22:50
 */
public class UpLoadResponseCommandDecoder implements CommandDecoder<UploadResponseCommand> {


    /**
     * @param content
     * @return
     */
    @Override
    public UploadResponseCommand decoder(byte[] content) {
        UploadResponseCommand responseCommand = new UploadResponseCommand();
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeBytes(content);
        responseCommand.setType(buffer.readShort());
        responseCommand.setStatus(buffer.readInt());
        int fileIdLen = buffer.readInt();
        byte[] fileIdBytes = new byte[fileIdLen];
        buffer.readBytes(fileIdBytes);
        responseCommand.setFileId(new String(fileIdBytes, StandardCharsets.UTF_8));
        buffer.release();
        return responseCommand;
    }
}
