package com.y.common.codec.download;

import com.y.common.codec.CommandDecoder;
import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


/**
 *  totalbytes + type + status + fileContentLen + fileContent
 *  this.setTotalBytes(8 + 2 + 4 + 4 + fileContent.length);
 */
public class DownloadResponseCommandDecoder  implements CommandDecoder<DownloadResponseCommand> {

    @Override
    public DownloadResponseCommand decoder(byte[] content) {
        DownloadResponseCommand responseCommand = new DownloadResponseCommand();
        ByteBuf byteBuf = Unpooled.buffer().writeBytes(content);
        short typeValue = byteBuf.readShort();
        responseCommand.setType(typeValue);
        int status = byteBuf.readInt();
        responseCommand.setStatus(status);
        int fileContentLen = byteBuf.readInt();
        byte[] bytes = new byte[fileContentLen];
        byteBuf.readBytes(bytes);
        responseCommand.setFileContent(bytes);
        byteBuf.release();
        return responseCommand;
    }


}
