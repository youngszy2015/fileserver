package com.y.client.connection;

import com.y.client.handler.ClientHandler;
import com.y.common.exception.CommonException;
import com.y.common.handler.Decoder;
import com.y.common.handler.Encoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private static FixedChannelPool fixedChannelPool = null;


    public static void initPool(Bootstrap bootstrap, ChannelPoolHandler channelPoolHandler, int size) {
        if (fixedChannelPool == null) {
            fixedChannelPool = new FixedChannelPool(bootstrap, channelPoolHandler, size);
        }
    }

    public static Channel getChannel() throws CommonException, InterruptedException {
        Future<Channel> future = fixedChannelPool.acquire().sync();
        if (future.isSuccess()) {
            try {
                Channel channel = future.get();
                if (channel.isWritable()) {
                    logger.info("获取链接:" + channel.id().asShortText());
                    return channel;
                }
                throw new CommonException("获取链接不可用");
            } catch (Exception e) {
                throw new CommonException("获取链接err:", e);
            }
        } else {
            return null;
        }
    }


    public static void releaseChannel(Channel channel) {
        Future<Void> release = fixedChannelPool.release(channel);

    }
}
