package org.huawei.task.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.huawei.task.dto.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@RequiredArgsConstructor
public class ClientHandler extends SimpleChannelInboundHandler<Object> {

    private final String clientName;
    private Channel channel;
    @Getter
    private boolean shutdownInvoked = false;
    BlockingQueue<PutResponse> putResps = new LinkedBlockingQueue<>();
    BlockingQueue<GetResponse> getResps = new LinkedBlockingQueue<>();
    BlockingQueue<ShutdownResponse> shtResps = new LinkedBlockingQueue<>();

    public PutResponse putValueToMap(KeyValue kv) throws Exception {
        PutRequest req = PutRequest.builder()
                .key(kv.getKey())
                .value(kv.getValue())
                .build();
        channel.writeAndFlush(req);
        boolean interrupted = false;
        PutResponse resp;
        for (; ; ) {
            try {
                resp = putResps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
        return resp;
    }

    public GetResponse getValueFromServer(Integer key) {
        GetRequest req = GetRequest.builder()
                .key(key)
                .build();
        channel.writeAndFlush(req);
        boolean interrupted = false;
        GetResponse resp;
        while (true) {
            try {
                resp = getResps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }
        if (interrupted)
            Thread.currentThread().interrupt();
        return resp;
    }

    public void shutdown() {
        ShutdownRequest req = ShutdownRequest.builder()
                .client(clientName)
                .build();
        channel.writeAndFlush(req);
        boolean interrupted = false;
        while (true) {
            try {
                shtResps.take();
                break;
            } catch (InterruptedException ignore) {
                interrupted = true;
            }
        }
        if (interrupted)
            Thread.currentThread().interrupt();
        shutdownInvoked = true;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (o instanceof PutResponse)
            putResps.put((PutResponse) o);
        if (o instanceof GetResponse)
            getResps.put((GetResponse) o);
        if (o instanceof ShutdownResponse) {
            var shRes = (ShutdownResponse) o;
            if (shRes.getClient().equals(clientName))
                shtResps.put((ShutdownResponse) o);
            else {
                shutdownInvoked = true;
            }
        }
    }
}
