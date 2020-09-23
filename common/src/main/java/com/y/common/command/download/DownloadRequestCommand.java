package com.y.common.command.download;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.AbstractCommand;
import com.y.common.command.CommandType;
import com.y.common.exception.CommonException;

import java.nio.charset.StandardCharsets;

public class DownloadRequestCommand extends AbstractCommand {

    private int fileIdLength;

    private String fileId;

    public DownloadRequestCommand() {
        super(CommandType.DOWNLOAD_REQUEST.getValue());
    }

    public DownloadRequestCommand(String fileId) {
        super(CommandType.DOWNLOAD_REQUEST.getValue());
        this.fileIdLength = fileId.getBytes(StandardCharsets.UTF_8).length;
        this.fileId = fileId;
        this.setTotalBytes(8 + 2 + 4 + fileId.getBytes(StandardCharsets.UTF_8).length);
    }

    @Override
    protected CommandDecoder getDecoder() throws CommonException {
        return CodecManager.getDecoderByCommandType(CommandType.DOWNLOAD_REQUEST);
    }

    @Override
    protected CommandEncoder getEncoder() throws CommonException {
        return CodecManager.getEncoderByCommandType(CommandType.DOWNLOAD_REQUEST);
    }

    public int getFileIdLength() {
        return fileIdLength;
    }

    public void setFileIdLength(int fileIdLength) {
        this.fileIdLength = fileIdLength;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "DownloadRequestCommand{" +
                "fileIdLength=" + fileIdLength +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
