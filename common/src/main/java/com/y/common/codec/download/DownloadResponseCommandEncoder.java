package com.y.common.codec.download;

import com.y.common.codec.CommandEncoder;
import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 * totalbytes + type + status + fileContentLen + fileContent
 * this.setTotalBytes(8 + 2 + 4 + 4 + fileContent.length);
 */
public class DownloadResponseCommandEncoder implements CommandEncoder<DownloadResponseCommand> {

    @Override
    public byte[] encoder(DownloadResponseCommand t) {
        ByteBuf buffer = Unpooled.buffer((int) t.getTotalBytes());
        buffer.writeLong(t.getTotalBytes());
        buffer.writeShort(t.getType().getValue());
        buffer.writeInt(t.getStatus());
        buffer.writeInt(t.getFileContentLen());
        buffer.writeBytes(t.getFileContent());
        byte[] bytes = new byte[buffer.readableBytes()];
        buffer.readBytes(bytes,buffer.readerIndex(),buffer.writerIndex());
        buffer.release();
        return bytes;
    }
}
