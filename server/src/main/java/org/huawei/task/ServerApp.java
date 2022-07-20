package org.huawei.task;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.huawei.task.network.ServerInitializer;

import java.util.concurrent.ConcurrentHashMap;

public class ServerApp {
    static final int PORT = Integer.parseInt(System.getProperty("port", "8463"));
    static final int PARENT_THREADS = Integer.parseInt(System.getProperty("parent_threads", "1"));

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parentGroup = new NioEventLoopGroup(PARENT_THREADS);
        EventLoopGroup childGroup = new NioEventLoopGroup();
        ConcurrentHashMap<Integer, String> keyValueMap = new ConcurrentHashMap<>();
        try {
            ServerBootstrap bootStrap = new ServerBootstrap();
            bootStrap.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerInitializer(keyValueMap));

            bootStrap.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
