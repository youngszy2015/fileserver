package com.y.server;

import com.y.common.codec.CodecManager;
import com.y.common.exception.CommonException;
import com.y.common.handler.Decoder;
import com.y.common.handler.Encoder;
import com.y.server.file.FileManager;
import com.y.server.handler.ServerHandler;
import com.y.server.config.Config;
import com.y.server.exception.ServerException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger log = LoggerFactory.getLogger(Server.class);

    public static Config config = null;

    public static void init() throws ServerException, CommonException {
        Config config = new Config();
        config.parse();
        Server.config = config;
        CodecManager.register();
        FileManager.init();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new Decoder());
                        ch.pipeline().addLast(new Encoder());
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });

        ChannelFuture sync = null;
        try {
            sync = serverBootstrap.bind(config.getPort()).sync();
            sync.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("server init on port: " + config.getPort());
                    }
                }
            });
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws ServerException, CommonException {
        Server.init();
    }

}
