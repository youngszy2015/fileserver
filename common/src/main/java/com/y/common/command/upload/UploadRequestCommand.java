package com.y.common.command.upload;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.AbstractCommand;
import com.y.common.command.CommandType;
import com.y.common.exception.CommonException;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UploadRequestCommand extends AbstractCommand {

    private String fileName;

    private int fileNameLength;

    private String fileSuffix;

    private short fileSuffixLength;

    private byte[] fileContent;

    private int fileContentLength;

    public UploadRequestCommand() {
        super(CommandType.UPLOAD_REQUEST.getValue());
    }

    public UploadRequestCommand(String fileName, String fileSuffix, byte[] fileContent) {
        super(CommandType.UPLOAD_REQUEST.getValue());
        this.fileName = fileName;
        this.fileNameLength = fileName.getBytes(StandardCharsets.UTF_8).length;
        this.fileSuffix = fileSuffix;
        this.fileSuffixLength = (short) fileSuffix.getBytes(StandardCharsets.UTF_8).length;
        this.fileContent = fileContent;
        this.fileContentLength = fileContent.length;
        this.setTotalBytes(8 + 2 + 4 + this.fileNameLength + 2 + this.fileSuffixLength + 4 + this.fileContentLength);
    }

    @Override
    protected CommandDecoder getDecoder() throws CommonException {
        return CodecManager.getDecoderByCommandType(CommandType.UPLOAD_REQUEST);
    }

    @Override
    protected CommandEncoder getEncoder() throws CommonException {
        return CodecManager.getEncoderByCommandType(CommandType.UPLOAD_REQUEST);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public int getFileNameLength() {
        return fileNameLength;
    }

    public void setFileNameLength(int fileNameLength) {
        this.fileNameLength = fileNameLength;
    }

    public short getFileSuffixLength() {
        return fileSuffixLength;
    }

    public void setFileSuffixLength(short fileSuffixLength) {
        this.fileSuffixLength = fileSuffixLength;
    }

    public int getFileContentLength() {
        return fileContentLength;
    }

    public void setFileContentLength(int fileContentLength) {
        this.fileContentLength = fileContentLength;
    }

    @Override
    public String toString() {
        return "UploadRequestCommand{" +
                "fileName='" + fileName + '\'' +
                ", fileNameLength=" + fileNameLength +
                ", fileSuffix='" + fileSuffix + '\'' +
                ", fileSuffixLength=" + fileSuffixLength +
                ", fileContentLength=" + fileContentLength +
                '}';
    }
}
