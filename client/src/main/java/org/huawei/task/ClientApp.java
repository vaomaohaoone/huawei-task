package org.huawei.task;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.lang3.RandomStringUtils;
import org.huawei.task.network.ClientHandler;
import org.huawei.task.network.ClientInitializer;
import org.huawei.task.service.ByeListener;
import org.huawei.task.service.ClientAppHelper;
import org.huawei.task.service.ConsoleService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientApp {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    // Get server ports from property, by "|" delimiter
    static final int[] PORTS = Arrays.stream(System.getProperty("ports", "8463").split("\\|")).mapToInt(Integer::parseInt).toArray();

    public static void main(String[] args) throws InterruptedException {
        var client = RandomStringUtils.randomAlphabetic(10);
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientInitializer(client));
        List<ClientHandler> handlers = new ArrayList<>();
        List<Channel> channels = new ArrayList<>();
        for (int port : PORTS) {
            Channel c = bootstrap.connect(HOST, port).sync().channel();
            channels.add(c);
            handlers.add(c.pipeline().get(ClientHandler.class));
        }
        ClientAppHelper clientAppHelper = new ClientAppHelper(handlers, channels);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
                new ByeListener(clientAppHelper, group),
                0, 100, TimeUnit.MILLISECONDS
        );
        var consoleService = new ConsoleService(clientAppHelper);
        consoleService.runConsole();
    }

}
