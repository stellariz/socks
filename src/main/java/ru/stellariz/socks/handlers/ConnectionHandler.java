package ru.stellariz.socks.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import ru.stellariz.socks.exception.ConnectionException;
import ru.stellariz.socks.handlers.connection.ConnectionMessageType;
import ru.stellariz.socks.handlers.connection.ConnectionRequestChainProcessor;
import ru.stellariz.socks.handlers.response.BindResponseMessageOnSucceedHandler;
import ru.stellariz.socks.handlers.response.ConnectionResponseMessageHandler;
import ru.stellariz.socks.handlers.response.ConnectionResponseMessageOnFailureHandler;
import ru.stellariz.socks.handlers.response.ConnectionResponseMessageOnSucceedHandler;

public class ConnectionHandler {
    private static final int SO_DEFAULT_TIMEOUT = 2 * 60 * 100;

    private static final int BUFFER_SIZE = 1024 * 1024;

    private static final ConnectionRequestChainProcessor SOCKS4_PROCESSOR =
            ConnectionRequestChainProcessor.buildSocks4ConnectionMessageProcessorChain();

    private static final Map<ConnectionMessageType, ConnectionResponseMessageHandler> connectionResponseHandlersMap =
            Map.of(
                    ConnectionMessageType.CONNECT, new ConnectionResponseMessageOnSucceedHandler(),
                    ConnectionMessageType.BIND, new BindResponseMessageOnSucceedHandler()
            );

    private static final ConnectionResponseMessageHandler onFailureResponseHandler =
            new ConnectionResponseMessageOnFailureHandler();

    private final Socket client;
    private volatile boolean closed;

    public ConnectionHandler(Socket client) {
        this.client = client;
        try (var isOrigin = client.getInputStream();
             var osOrigin = client.getOutputStream();
             var destSocket = handleClientRequest(isOrigin, osOrigin)) {

            // TODO(r.popov): extract proxy logic into new class
            try (var isDest = destSocket.getInputStream();
                 var osDest = destSocket.getOutputStream()) {
                var t1 = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isOrigin, osDest));
                var t2 = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isDest, osOrigin));

                t1.join();
                t2.join();

                client.close();
            }
        } catch (ConnectionException | IOException | InterruptedException ex) {
            //TODO(r.popov): close client connection correctly
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(ex);
        }
    }

    private void sendMessageFromSocketToSocket(InputStream is, OutputStream os) {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (!closed) {
            try {
                int bytesRead;
                while ((bytesRead = is.read(buffer)) > 0) {
                    os.write(buffer, 0, bytesRead);
                    os.flush();
                }
                if (bytesRead == -1) {
                    closed = true;
                }
            } catch (IOException ex) {
                closed = true;
            }
        }
    }

    private Socket handleClientRequest(InputStream is, OutputStream os) throws IOException {
        var connectionRequestContext = SOCKS4_PROCESSOR.processRequest(is);
        if (connectionRequestContext.exception() != null) {
            onFailureResponseHandler.sendResponse(os, connectionRequestContext);
            // Выбрасываем исключение и закрываем сокеты с помощью try-with-resources
            throw connectionRequestContext.exception();
        }

        System.out.printf("Thread [%s]: Client %s send hello message\n",
                Thread.currentThread().getName(), connectionRequestContext.userId());

        var responseHandler =
                connectionResponseHandlersMap.get(connectionRequestContext.messageType());
        var port = connectionRequestContext.dstPort();
        var inetAddress = connectionRequestContext.resolvedDstAddress();
        Socket destSocket;
        try {
            //TODO(r.popov): вынести логику в хэндлеры аналогично тому как мы обрабатывается connectiontype
            if (connectionRequestContext.messageType() == ConnectionMessageType.CONNECT) {
                destSocket = new Socket(inetAddress, port);
                System.out.printf("Thread [%s]: Connection to %s:%d established\n",
                        Thread.currentThread().getName(), inetAddress.getHostName(), port);
                responseHandler.sendResponse(os, connectionRequestContext);
            } else {
                // Открываем сокет и слушаем на порте, полученном от клиента
                try (var additionalServer = new ServerSocket(connectionRequestContext.dstPort())) {
                    System.out.printf("Thread [%s]: Connection for incoming requests is opened on port: [%d]\n",
                            Thread.currentThread().getName(), connectionRequestContext.dstPort());
                    responseHandler.sendResponse(os, connectionRequestContext);
                    destSocket = additionalServer.accept();
                }
            }
            destSocket.setSoTimeout(SO_DEFAULT_TIMEOUT);
        } catch (IOException ex) {
            onFailureResponseHandler.sendResponse(os, connectionRequestContext);
            System.err.printf("Thread [%s]: Unable to open connection\n", Thread.currentThread().getName());
            throw ex;
        }
        return destSocket;
    }
}
