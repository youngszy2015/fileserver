package com.y.common.command;

import com.y.common.codec.*;
import com.y.common.exception.CommonException;

public abstract class AbstractCommand implements Command {

    private short type;

    private long totalBytes;

    public AbstractCommand(short type) {
        this.type = type;
    }


    @Override
    public CommandEncoder getCommandEncoder() throws CommonException {
        return getEncoder();
    }

    @Override
    public CommandDecoder getCommandDecoder() throws CommonException {
        return getDecoder();
    }

    protected abstract CommandDecoder getDecoder() throws CommonException;

    protected abstract CommandEncoder getEncoder() throws CommonException;

    public CommandType getType() {
        return CommandType.valueOf(type);
    }

    public void setType(short type) {
        this.type = type;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes = totalBytes;
    }
}
