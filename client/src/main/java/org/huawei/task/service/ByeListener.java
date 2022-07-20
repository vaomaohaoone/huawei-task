package org.huawei.task.service;

import io.netty.channel.ChannelOutboundInvoker;
import io.netty.channel.EventLoopGroup;
import org.huawei.task.network.ClientHandler;

import static org.huawei.task.MessageText.BYE_TEXT;

/**
 * Listens BYE or SHUTDOWN event
 * */
public class ByeListener implements Runnable {

    private final ClientAppHelper handlerFactory;
    private final EventLoopGroup group;

    public ByeListener(ClientAppHelper handlerFactory, EventLoopGroup group) {
        this.handlerFactory = handlerFactory;
        this.group = group;
    }

    @Override
    public void run() {
        if (handlerFactory.isQuitInvoked() || handlerFactory.getHandlers().stream()
                .anyMatch(ClientHandler::isShutdownInvoked)) {
            handlerFactory.getChannels().forEach(ChannelOutboundInvoker::close);
            group.shutdownGracefully();
            System.out.println(BYE_TEXT);
            System.exit(0);
        }
    }
}
