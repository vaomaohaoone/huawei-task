package org.huawei.task.service;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.huawei.task.network.ClientHandler;

import java.util.List;

/**
 * Holds handlers and channels, stores a QUIT event flag
 * */
@RequiredArgsConstructor
@Getter
public class ClientAppHelper {
    private final List<ClientHandler> handlers;
    private final List<Channel> channels;
    @Setter
    private boolean quitInvoked = false;

    public ClientHandler getHandler(int key) {
        return handlers.get(key % handlers.size());
    }

}
