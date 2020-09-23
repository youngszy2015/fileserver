package com.y.common.command.upload;

import com.y.common.codec.CodecManager;
import com.y.common.codec.CommandDecoder;
import com.y.common.codec.CommandEncoder;
import com.y.common.command.AbstractCommand;
import com.y.common.command.CommandType;
import com.y.common.exception.CommonException;

import java.nio.charset.StandardCharsets;

/**
 * @Author: Young
 * @DATE: 2020/9/21 22:34
 */
public class UploadResponseCommand extends AbstractCommand {

    private int status;

    private String fileId;


    public UploadResponseCommand() {
        super(CommandType.UPLOAD_RESPONSE.getValue());
    }

    /**
     *
     * @param status
     * @param fileId
     */



    public UploadResponseCommand(int status, String fileId) {
        super(CommandType.UPLOAD_RESPONSE.getValue());
        this.status = status;
        this.fileId = fileId;
        //type status fileIdLen fileIdBytes
        this.setTotalBytes(8 + 2 + 4 + 4 + this.fileId.getBytes(StandardCharsets.UTF_8).length);
    }

    @Override
    protected CommandDecoder getDecoder() throws CommonException {
        return CodecManager.getDecoderByCommandType(CommandType.UPLOAD_RESPONSE);
    }

    @Override
    protected CommandEncoder getEncoder() throws CommonException {
        return CodecManager.getEncoderByCommandType(CommandType.UPLOAD_RESPONSE);
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public String toString() {
        return "UploadResponseCommand{" +
                "status=" + status +
                ", fileId='" + fileId + '\'' +
                '}';
    }
}
