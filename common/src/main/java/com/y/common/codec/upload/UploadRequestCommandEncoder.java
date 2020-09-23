package com.y.common.codec.upload;

import com.y.common.codec.CommandEncoder;
import com.y.common.command.upload.UploadRequestCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @Author Young
 * @Date 2020-9-21 21:31:42
 * <p>
 * 文件上传请求编码
 * |---------------|---------------|---------------|------------------------|------------------|-----------------|-------------------|
 *    totalBytes        type            fileNameLength     fileSuffixLength       fileSuffix       fileContentLength    fileContent
 * |---------------|---------------|---------------|-----------------------|------------------|------------------|-------------------|
 */
public class UploadRequestCommandEncoder implements CommandEncoder<UploadRequestCommand> {


    @Override
    public byte[] encoder(UploadRequestCommand t) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeLong(t.getTotalBytes());
        buffer.writeShort(t.getType().getValue());
        buffer.writeInt(t.getFileNameLength());
        buffer.writeBytes(t.getFileName().getBytes(StandardCharsets.UTF_8));
        buffer.writeShort(t.getFileSuffixLength());
        buffer.writeBytes(t.getFileSuffix().getBytes(StandardCharsets.UTF_8));
        buffer.writeInt(t.getFileContentLength());
        buffer.writeBytes(t.getFileContent());
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes,buffer.readerIndex(),buffer.writerIndex());
        buffer.release();
        return bytes;
    }
}