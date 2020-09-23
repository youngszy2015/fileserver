package com.y.common.codec.download;

import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import com.y.common.command.upload.UploadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

public class DownloadRequestCommandDecoder implements CommandDecoder<DownloadRequestCommand> {

    /**
     * @param content
     * @return
     */
    @Override
    public DownloadRequestCommand decoder(byte[] content) {
        DownloadRequestCommand downloadRequestCommand = new DownloadRequestCommand();
        ByteBuf byteBuf = Unpooled.buffer().writeBytes(content);
        short typeValue = byteBuf.readShort();
        downloadRequestCommand.setType(typeValue);
        int fileIdLength = byteBuf.readInt();
        byte[] bytes = new byte[fileIdLength];
        byteBuf.readBytes(bytes);
        downloadRequestCommand.setFileId(new String(bytes));
        byteBuf.release();
        return downloadRequestCommand;
    }


}
