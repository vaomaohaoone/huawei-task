package org.huawei.task.service;

import io.netty.channel.ChannelOutboundInvoker;
import lombok.RequiredArgsConstructor;
import org.huawei.task.dto.KeyValue;
import org.huawei.task.enums.Command;
import org.huawei.task.network.ClientHandler;

import java.util.Scanner;

import static org.huawei.task.MessageText.*;
import static org.huawei.task.enums.Command.QUIT;
import static org.huawei.task.enums.Command.SHUTDOWN;

@RequiredArgsConstructor
public class ConsoleService {

    private final ClientHandlerFactory handlerFactory;

    public void runConsole() {
        var run = true;
        while (run) {
            try {
                System.out.println(WELCOME_TEXT);
                Scanner sc = new Scanner(System.in);
                String commandText = sc.nextLine();
                var splitText = commandText.split("\\s+");
                if (splitText.length == 2) {
                    var command = Command.valueOf(splitText[0].toUpperCase());
                    var argument = splitText[1];
                    switch (command) {
                        case GET: {
                            var key = Integer.parseInt(argument);
                            var response = handlerFactory.getHandler(key).getValueFromServer(key);
                            if (response.isPresent())
                                System.out.println(response.getValue());
                            else
                                System.out.println(ERR_TEXT);
                            break;
                        }
                        case PUT: {
                            var kvString = argument.split(":");
                            if (kvString.length == 2) {
                                KeyValue kv = new KeyValue(Integer.parseInt(kvString[0]), kvString[1]);
                                System.out.println(PUT_SUCCESS +
                                        handlerFactory.getHandler(kv.getKey()).putValueToMap(kv));
                            } else
                                throw new IllegalArgumentException(ERR_TEXT);
                            break;
                        }
                        default:
                            throw new IllegalArgumentException(ERR_TEXT);
                    }
                } else {
                    var byeCommand = Command.valueOf(commandText.toUpperCase());
                    // Avoiding existing another one-word command
                    if (byeCommand == SHUTDOWN || byeCommand == QUIT) {
                        if (byeCommand == SHUTDOWN)
                            handlerFactory.getHandlers().parallelStream().forEach(ClientHandler::shutdown);
                        if (byeCommand == QUIT)
                            handlerFactory.setQuitInvoked(true);
                        run = false;
                    } else
                        throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException iex) {
                System.out.println(ERR_TEXT);
            } catch (Exception ex) {
                System.out.println(ERR_TEXT + ex.getMessage());
            }
        }
    }
}

