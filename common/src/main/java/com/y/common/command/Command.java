package com.y.common.command;

import com.y.common.codec.*;
import com.y.common.exception.CommonException;

import java.io.Serializable;

public interface Command extends Serializable {

    public CommandType getType();

   public CommandEncoder getCommandEncoder() throws CommonException;


    public CommandDecoder getCommandDecoder() throws CommonException;

}
