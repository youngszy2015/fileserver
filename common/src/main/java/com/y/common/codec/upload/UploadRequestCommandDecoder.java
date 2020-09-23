package com.y.common.codec.upload;

import com.y.common.codec.CommandDecoder;
import com.y.common.command.upload.UploadRequestCommand;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Young
 * @DATE: 2020/9/21 22:25
 */
public class UploadRequestCommandDecoder implements CommandDecoder<UploadRequestCommand> {


    @Override
    public UploadRequestCommand decoder(byte[] content) {
        UploadRequestCommand requestCommand = new UploadRequestCommand();
        ByteBuf byteBuf = Unpooled.buffer().writeBytes(content);
//        long totalBytes = byteBuf.readLong();
//        requestCommand.setTotalBytes(totalBytes);
        //type
        short type = byteBuf.readShort();
        requestCommand.setType(type);
        //fileName
        int fileNameCount = byteBuf.readInt();
        byte[] fileNameBytes = new byte[fileNameCount];
        byteBuf.readBytes(fileNameBytes);
        requestCommand.setFileNameLength(fileNameCount);
        requestCommand.setFileName(new String(fileNameBytes, StandardCharsets.UTF_8));
        //fileSuffix
        short suffixCount = byteBuf.readShort();
        byte[] fileSuffixBytes = new byte[suffixCount];
        byteBuf.readBytes(fileSuffixBytes);
        requestCommand.setFileSuffixLength(suffixCount);
        requestCommand.setFileSuffix(new String(fileSuffixBytes,StandardCharsets.UTF_8));
        //fileContent
        int contentLen = byteBuf.readInt();
        byte[] fileContentBytes = new byte[contentLen];
        byteBuf.readBytes(fileContentBytes);
        requestCommand.setFileContentLength(contentLen);
        requestCommand.setFileContent(fileContentBytes);
        byteBuf.release();
        return requestCommand;
    }


}
