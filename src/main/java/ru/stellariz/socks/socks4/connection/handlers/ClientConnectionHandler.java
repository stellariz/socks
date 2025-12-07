package ru.stellariz.socks.socks4.connection.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.common.OnFailedConnectionHandler;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.session.SessionHandler;
import ru.stellariz.socks.common.utils.OperationType;
import ru.stellariz.socks.socks4.connection.Socks4ConnectionRequestChainProcessor;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

/**
 * Класс, обрабатывающий соединение клиента и устанавливающий соединение со сторонним сервером
 */
public class ClientConnectionHandler {
    private static final int SO_DEFAULT_TIMEOUT = 2 * 60 * 100;

    private static final Socks4ConnectionRequestChainProcessor SOCKS4_PROCESSOR =
            new Socks4ConnectionRequestChainProcessor();

    private static final Map<OperationType, ConnectionTypeHandler<ConnectionRequestContext>> connectionHandlersMap =
            Map.of(
                    OperationType.CONNECT, new SucceedConnectionHandler(),
                    OperationType.BIND, new SucceedBindHandler()
            );

    private static final OnFailedConnectionHandler onFailureHandler =
            new FailedConnectionHandler();

    public ClientConnectionHandler(Socket client) {
        try (var isOrigin = client.getInputStream();
             var osOrigin = client.getOutputStream();
             client;
             var destSocket = handleClientRequest(isOrigin, osOrigin)) {
            var session = new SessionHandler(client, destSocket);
        } catch (ConnectionException | IOException | InterruptedException ex) {
            System.err.printf("Thread [%s]: Error occurred during connection with client: %s\n",
                    Thread.currentThread().getName(), ex.getMessage());
        }
    }


    private Socket handleClientRequest(InputStream clientIs, OutputStream clientOs) throws IOException {
        var connectionRequestContext = SOCKS4_PROCESSOR.buildContextFromClientRequest(clientIs);
        if (connectionRequestContext.exception() != null) {
            onFailureHandler.sendFailedConnectionMessage(clientOs);
            // Выбрасываем исключение и закрываем сокеты с помощью try-with-resources
            throw connectionRequestContext.exception();
        }

        System.out.printf("Thread [%s]: Client %s send hello message\n",
                Thread.currentThread().getName(), connectionRequestContext.userId());

        var connectionHandler =
                connectionHandlersMap.get(connectionRequestContext.operationType());

        Socket destSocket;
        try {
            destSocket = connectionHandler.createSocket(clientOs, connectionRequestContext);
            destSocket.setSoTimeout(SO_DEFAULT_TIMEOUT);
        } catch (IOException ex) {
            onFailureHandler.sendFailedConnectionMessage(clientOs);
            System.err.printf("Thread [%s]: Unable to open connection\n", Thread.currentThread().getName());
            throw ex;
        }
        return destSocket;
    }
}
