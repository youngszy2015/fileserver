package com.y.client;

import com.y.client.connection.ConnectionPool;
import com.y.client.handler.ClientHandler;
import com.y.common.codec.CodecManager;
import com.y.common.command.download.DownloadRequestCommand;
import com.y.common.command.download.DownloadResponseCommand;
import com.y.common.command.upload.UploadRequestCommand;
import com.y.common.command.upload.UploadResponseCommand;
import com.y.common.exception.CommonException;
import com.y.common.handler.Decoder;
import com.y.common.handler.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static com.y.client.InvokeFuture.INVOKE_FUTURE;

public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private String connectString;

    public Client(String connectString) {
        this.connectString = connectString;
    }

    public void init() throws CommonException {
        if (this.connectString == null) {
            throw new IllegalArgumentException("connectString must be not null");
        }
        CodecManager.register();
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup)
                .remoteAddress(this.connectString.split(":")[0], Integer.parseInt(this.connectString.split(":")[1]))
                .channel(NioSocketChannel.class);

        ConnectionPool.initPool(bootstrap, new ChannelPoolHandler() {
            @Override
            public void channelReleased(Channel ch) throws Exception {
                logger.info("pool release channel:" + ch.id().asShortText());
            }

            @Override
            public void channelAcquired(Channel ch) throws Exception {
                logger.info("pool acquire channel:" + ch.id().asShortText());
            }

            @Override
            public void channelCreated(Channel ch) throws Exception {
                logger.info("pool create channel:" + ch.id().asShortText());
                ch.pipeline().addLast(new Decoder());
                ch.pipeline().addLast(new Encoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        }, 16);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                bossGroup.shutdownGracefully();
            }
        });
    }


    public static UploadResponseCommand upload(File file) throws Exception {
        String fullName = file.getName();
        String[] split = fullName.split("\\.");
        String name = split[0];
        String suffix = split[1];
        byte[] bytes = FileUtils.readFileToByteArray(file);
        return upload(name, suffix, bytes);
    }


    public static UploadResponseCommand upload(String fileName, String fileSuffix, byte[] fileContent) throws Exception {
        UploadRequestCommand uploadRequestCommand = new UploadRequestCommand(fileName, fileSuffix, fileContent);
        Channel channel = ConnectionPool.getChannel();
        if (channel != null) {
            InvokeFuture invokeFuture = new InvokeFuture();
            channel.attr(INVOKE_FUTURE).set(invokeFuture);
            ChannelFuture channelFuture = channel.writeAndFlush(uploadRequestCommand);
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info("request success:{}", uploadRequestCommand);
                } else {
                    logger.error("request error:{}", uploadRequestCommand);
                    future.cause().printStackTrace();
                }
            });
            invokeFuture.waitResponse();
            return (UploadResponseCommand) invokeFuture.getResult();
        } else {
            logger.error("request acquire channel failed,{}", uploadRequestCommand);
        }
        //失败状态
        return new UploadResponseCommand(1, "");
    }

    public static DownloadResponseCommand download(String fileId) throws CommonException, InterruptedException {
        DownloadRequestCommand downloadRequestCommand = new DownloadRequestCommand(fileId);
        Channel channel = ConnectionPool.getChannel();
        if (null != channel && channel.isActive() && channel.isWritable()) {
            ChannelFuture channelFuture = channel.writeAndFlush(downloadRequestCommand);
            InvokeFuture invokeFuture = new InvokeFuture();
            channel.attr(INVOKE_FUTURE).set(invokeFuture);
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    logger.info("download request success:{}", downloadRequestCommand);
                } else {
                    logger.error("download request err:{}", downloadRequestCommand);
                    throw new CommonException(future.cause());
                }
            });
            invokeFuture.waitResponse();
            return (DownloadResponseCommand) invokeFuture.getResult();
        }
        return new DownloadResponseCommand(1, 0, new byte[0]);
    }
}
