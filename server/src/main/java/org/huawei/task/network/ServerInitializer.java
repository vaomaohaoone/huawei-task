package org.huawei.task.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ConcurrentHashMap<Integer, String> keyValueMap;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        p.addLast(new ObjectEncoder());
        p.addLast(new ServerHandler(keyValueMap));
    }
}
