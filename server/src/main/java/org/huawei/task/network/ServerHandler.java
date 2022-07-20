package org.huawei.task.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.huawei.task.dto.*;

import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final ConcurrentHashMap<Integer, String> keyValueMap;
    private static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // Netty logger was used
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object o) throws Exception {
        if (o instanceof PutRequest) {
            var cmd = (PutRequest) o;
            if (keyValueMap.get(cmd.getKey()) == null) {
                keyValueMap.putIfAbsent(cmd.getKey(), cmd.getValue());
                ctx.write(new PutResponse(true, cmd.getKey()));
            } else
                ctx.write(new PutResponse(false, cmd.getKey()));
        }
        if (o instanceof GetRequest) {
            var cmd = (GetRequest) o;
            var val = keyValueMap.get(cmd.getKey());
            if (val != null)
                ctx.write(new GetResponse(true, val));
            else
                ctx.write(new GetResponse(false, null));
        }
        if (o instanceof ShutdownRequest) {
            var cmd = (ShutdownRequest) o;
            logger.info("---Shutdown from client " + cmd.getClient() + " invoked");
            group.writeAndFlush(new ShutdownResponse(cmd.getClient())).await().sync();
            logger.info("---Shutdown from client " + cmd.getClient() + " ended");
            ctx.channel().close();
            ctx.channel().parent().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        group.add(channel);
        logger.info(channel.remoteAddress() + "---Client connected, current group size: " + group.size());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info(channel.remoteAddress() + "---Client disconnected, current group size: " + group.size());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}
