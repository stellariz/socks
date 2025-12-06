package ru.stellariz.socks.socks5.connection.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.common.OnFailedConnectionHandler;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.session.SessionHandler;
import ru.stellariz.socks.common.utils.ConnectionMessageType;
import ru.stellariz.socks.socks5.connection.Socks5ConnectionRequestChainProcessor;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;
import ru.stellariz.socks.socks5.authentication.MethodAuthenticationSelector;
import ru.stellariz.socks.socks5.connection.handlers.authentication.AuthenticationHandlerOnFailureHandler;
import ru.stellariz.socks.socks5.connection.handlers.authentication.AuthenticationHandlerOnSucceedHandler;
import ru.stellariz.socks.socks5.connection.handlers.authentication.AuthenticationTypeHandler;
import ru.stellariz.socks.socks5.context.AuthenticationContext;

public class MainConnectionHandler {
    private static final int SO_DEFAULT_TIMEOUT = 2 * 60 * 100;

    private static final MethodAuthenticationSelector METHOD_AUTHENTICATION_SELECTOR =
            new MethodAuthenticationSelector();
    private static final Socks5ConnectionRequestChainProcessor SOCKS5_PROCESSOR =
            new Socks5ConnectionRequestChainProcessor();

    private static final Map<ConnectionMessageType, ConnectionTypeHandler<ConnectionRequestContext>> connectionHandlersMap =
            Map.of(
                    ConnectionMessageType.CONNECT, new ConnectionHandlerOnSucceedHandler(),
                    ConnectionMessageType.BIND, new BindConnectionOnSucceedHandler()
            );

    private static final OnFailedConnectionHandler onFailureConnectionHandler =
            new ConnectionHandlerOnFailureHandler();

    private static final AuthenticationTypeHandler<AuthenticationContext> onFailureAuthenticationHandler =
            new AuthenticationHandlerOnFailureHandler();
    private static final AuthenticationTypeHandler<AuthenticationContext> onSucceedAuthenticationHandler =
            new AuthenticationHandlerOnSucceedHandler();

    public MainConnectionHandler(Socket client) {
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
        System.out.printf("Thread [%s]: Client send authentication message\n", Thread.currentThread().getName());

        var authenticationContext =
                METHOD_AUTHENTICATION_SELECTOR.buildContextFromClientRequest(clientIs);
        if (authenticationContext.exception() != null) {
            onFailureAuthenticationHandler.choseMethodAndNotifyClient(authenticationContext, clientOs);
            // Выбрасываем исключение и закрываем сокеты с помощью try-with-resources
            throw authenticationContext.exception();
        }
        // Выбираем метод для авторизации
        var chosenAuthMethod =
                onSucceedAuthenticationHandler.choseMethodAndNotifyClient(authenticationContext, clientOs);
        System.out.printf("Thread [%s]: Chosen authentication: %s\n", Thread.currentThread().getName(), chosenAuthMethod);

        var connectionRequestContext = SOCKS5_PROCESSOR.buildContextFromClientRequest(clientIs);

        System.out.printf("Thread [%s]: Client send hello message\n", Thread.currentThread().getName());

        var connectionHandler =
                connectionHandlersMap.get(connectionRequestContext.messageType());

        Socket destSocket;
        try {
            destSocket = connectionHandler.createSocket(clientOs, connectionRequestContext);
            destSocket.setSoTimeout(SO_DEFAULT_TIMEOUT);
        } catch (IOException ex) {
            onFailureConnectionHandler.sendFailedConnectionMessage(clientOs);
            System.err.printf("Thread [%s]: Unable to open connection\n", Thread.currentThread().getName());
            throw ex;
        }
        return destSocket;
    }
}
