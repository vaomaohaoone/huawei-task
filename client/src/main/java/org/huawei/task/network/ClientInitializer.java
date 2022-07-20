package org.huawei.task.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final String client;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ObjectEncoder());
        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        p.addLast(new ClientHandler(client));
    }
}
