package com.y.common.command.download;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.AbstractCommand;
import com.y.common.command.CommandType;
import com.y.common.exception.CommonException;

import java.util.Arrays;

public class DownloadResponseCommand extends AbstractCommand {

    private int status;

    private int fileContentLen;

    private byte[] fileContent;


    public DownloadResponseCommand() {
        super(CommandType.DOWNLOAD_RESPONSE.getValue());
    }

    public DownloadResponseCommand(int status, int fileContentLen, byte[] fileContent) {
        super(CommandType.DOWNLOAD_RESPONSE.getValue());
        this.status = status;
        this.fileContentLen = fileContentLen;
        this.fileContent = fileContent;
        //totalbytes + type + status + fileContentLen + fileContent
        this.setTotalBytes(8 + 2 + 4 + 4 + fileContent.length);
    }

    @Override
    protected CommandDecoder getDecoder() throws CommonException {
         return CodecManager.getDecoderByCommandType(CommandType.DOWNLOAD_RESPONSE);
    }

    @Override
    protected CommandEncoder getEncoder() throws CommonException {
        return CodecManager.getEncoderByCommandType(CommandType.DOWNLOAD_RESPONSE);
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFileContentLen() {
        return fileContentLen;
    }

    public void setFileContentLen(int fileContentLen) {
        this.fileContentLen = fileContentLen;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    @Override
    public String toString() {
        return "DownloadResponseCommand{" +
                "status=" + status +
                ", fileContentLen=" + fileContentLen +
                '}';
    }
}
