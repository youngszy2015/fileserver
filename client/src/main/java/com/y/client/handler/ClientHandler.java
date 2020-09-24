package com.y.client.handler;

import com.y.common.InvokeFuture;
import com.y.common.command.download.DownloadResponseCommand;
import com.y.common.command.upload.UploadResponseCommand;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Young
 * @DATE: 2020/9/22 21:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof UploadResponseCommand) {
            try {
                UploadResponseCommand uploadResponseCommand = (UploadResponseCommand) msg;
                logger.info("receive upload response: " + uploadResponseCommand);
                InvokeFuture invokeFuture = ctx.channel().attr(InvokeFuture.INVOKE_FUTURE).get();
                invokeFuture.putResponse(uploadResponseCommand);
            } catch (Exception e) {
                UploadResponseCommand errResponse = new UploadResponseCommand(1, "");
                InvokeFuture invokeFuture = ctx.channel().attr(InvokeFuture.INVOKE_FUTURE).get();
                invokeFuture.putResponse(errResponse);
            }
        } else if (msg instanceof DownloadResponseCommand) {
            DownloadResponseCommand responseCommand = (DownloadResponseCommand) msg;
            logger.info("receive upload response: " + responseCommand);
            InvokeFuture invokeFuture = ctx.channel().attr(InvokeFuture.INVOKE_FUTURE).get();
            invokeFuture.putResponse(responseCommand);
        }
    }
}
