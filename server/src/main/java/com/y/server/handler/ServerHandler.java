package com.y.server.handler;

import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import com.y.common.command.upload.UploadRequestCommand;
import com.y.common.command.upload.UploadResponseCommand;
import com.y.server.config.Config;
import com.y.server.exception.ServerException;
import com.y.server.file.FileManager;
import io.netty.channel.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof UploadRequestCommand) {
            //上传文件请求
            UploadRequestCommand requestCommand = (UploadRequestCommand) msg;
            logger.info("receive uploadRequest: " + requestCommand);
            //存储文件 写回响应
            ctx.executor().execute(() -> {
                try {
                    String fileId1 = FileManager.flushFile(requestCommand);
                    UploadResponseCommand responseCommand = new UploadResponseCommand(0, fileId1);
                    ctx.channel().writeAndFlush(responseCommand);
                } catch (ServerException e) {
                    logger.error("write response err: {}", requestCommand, e);
                }
            });
        } else if (msg instanceof DownloadRequestCommand) {
            //下载请求
            DownloadRequestCommand downloadRequestCommand = (DownloadRequestCommand) msg;
            logger.info("receive download request: " + downloadRequestCommand);
            ctx.executor().execute(() -> {
                try {
                    //获取文件，返回
                    String fileId = downloadRequestCommand.getFileId();
                    byte[] content = FileManager.getFile(fileId);
                    if (null != content) {
                        ctx.channel().writeAndFlush(new DownloadResponseCommand(0, content.length, content));
                    } else {
                        ctx.channel().writeAndFlush(new DownloadResponseCommand(1, 0, new byte[0]));
                    }
                } catch (ServerException e) {
                    logger.error("write response err: {}", downloadRequestCommand, e);
                }
            });

        } else {
            logger.info("server handler receive other command type: " + msg.getClass());
        }
    }

}
