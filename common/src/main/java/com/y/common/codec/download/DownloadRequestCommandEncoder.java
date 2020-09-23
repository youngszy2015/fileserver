package com.y.common.codec.download;

import com.y.common.codec.CommandEncoder;
import com.y.common.command.download.DownloadRequestCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

public class DownloadRequestCommandEncoder implements CommandEncoder<DownloadRequestCommand> {

    @Override
    public byte[] encoder(DownloadRequestCommand t) {
        long totalBytes = t.getTotalBytes();
        byte[] bytes = new byte[(int) totalBytes];
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeLong(totalBytes);
        buffer.writeShort(t.getType().getValue());
        buffer.writeInt(t.getFileIdLength());
        buffer.writeBytes(t.getFileId().getBytes(StandardCharsets.UTF_8));
        buffer.readBytes(bytes);
        buffer.release();
        return bytes;
    }


}
