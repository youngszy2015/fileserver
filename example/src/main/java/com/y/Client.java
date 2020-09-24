package com.y;

import com.y.common.codec.CodecManager;
import com.y.common.exception.CommonException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

public class Client {

    public static void init() throws InterruptedException, CommonException {
        CodecManager.register();
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        bootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientCommandHandler());
                    }
                });

        try {
            ChannelFuture future = bootstrap.connect("localhost", 9091).sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        future.channel().writeAndFlush("request-01").addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if (future.isSuccess()) {
                                    System.out.println("write success");
                                } else {
                                    System.err.println("write failed");
                                    future.cause().printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
            TimeUnit.SECONDS.sleep(100);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
        }


    }

    public static void main(String[] args) throws CommonException, InterruptedException {
        Client.init();
    }
}