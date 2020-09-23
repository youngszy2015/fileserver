package com.y.common.codec.upload;

import com.y.common.codec.CommandEncoder;
import com.y.common.command.upload.UploadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Young
 * @DATE: 2020/9/21 22:33
 * <p>
 * 文件上传响应编码
 * |---------|------------------|------------------|-----------------|
 * type        status             fileIdLength      fileId
 * |---------|-----------------|------------------|------------------|
 */
public class UploadResponseCommandEncoder implements CommandEncoder<UploadResponseCommand> {
    @Override
    public byte[] encoder(UploadResponseCommand t) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeLong(t.getTotalBytes());
        buf.writeShort(t.getType().getValue());
        buf.writeInt(t.getStatus());
        byte[] fileId = t.getFileId().getBytes(StandardCharsets.UTF_8);
        buf.writeInt(fileId.length);
        buf.writeBytes(fileId);
        byte[] cmdBytes = new byte[buf.readableBytes()];
        buf.readBytes(cmdBytes, 0, buf.writerIndex());
        buf.release();
        return cmdBytes;
    }
}
