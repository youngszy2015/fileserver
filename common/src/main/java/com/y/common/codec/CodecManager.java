package com.y.common.codec;

import com.y.common.codec.download.DownloadRequestCommandDecoder;
import com.y.common.codec.download.DownloadRequestCommandEncoder;
import com.y.common.codec.download.DownloadResponseCommandDecoder;
import com.y.common.codec.download.DownloadResponseCommandEncoder;
import com.y.common.codec.upload.UpLoadResponseCommandDecoder;
import com.y.common.codec.upload.UploadRequestCommandDecoder;
import com.y.common.codec.upload.UploadRequestCommandEncoder;
import com.y.common.codec.upload.UploadResponseCommandEncoder;
import com.y.common.command.CommandType;
import com.y.common.exception.CommonException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class CodecManager {

    private static AtomicBoolean started = new AtomicBoolean(false);
    private static final Map<CommandType, CommandEncoder> ENCODERS = new ConcurrentHashMap<>();
    private static final Map<CommandType, CommandDecoder> DECODERS = new ConcurrentHashMap<>();


    public static void register() throws CommonException {
        if (started.compareAndSet(false, true)) {
            ENCODERS.put(CommandType.UPLOAD_REQUEST, new UploadRequestCommandEncoder());
            ENCODERS.put(CommandType.UPLOAD_RESPONSE, new UploadResponseCommandEncoder());
            ENCODERS.put(CommandType.DOWNLOAD_REQUEST, new DownloadRequestCommandEncoder());
            ENCODERS.put(CommandType.DOWNLOAD_RESPONSE, new DownloadResponseCommandEncoder());

            DECODERS.put(CommandType.UPLOAD_REQUEST, new UploadRequestCommandDecoder());
            DECODERS.put(CommandType.UPLOAD_RESPONSE, new UpLoadResponseCommandDecoder());
            DECODERS.put(CommandType.DOWNLOAD_REQUEST, new DownloadRequestCommandDecoder());
            DECODERS.put(CommandType.DOWNLOAD_RESPONSE, new DownloadResponseCommandDecoder());

        } else {
            throw new CommonException("CodecManager has start up");
        }
    }


    public static CommandEncoder getEncoderByCommandType(CommandType commandType) throws CommonException {
        ensureStart();
        CommandEncoder commandEncoder = ENCODERS.get(commandType);
        if (commandEncoder == null) {
            throw new CommonException("Encoder is not exists for " + commandType);
        }
        return commandEncoder;
    }

    public static CommandDecoder getDecoderByCommandType(CommandType commandType) throws CommonException {
        ensureStart();
        CommandDecoder commandDecoder = DECODERS.get(commandType);
        if (commandDecoder == null) {
            throw new CommonException("Decoder is not exists for " + commandType);
        }
        return commandDecoder;
    }

    private static void ensureStart() throws CommonException {
        if (!started.get()) {
            throw new CommonException("Codec manager not start up");
        }
    }


}
