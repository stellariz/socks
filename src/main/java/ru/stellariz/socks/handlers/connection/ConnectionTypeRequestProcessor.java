package ru.stellariz.socks.handlers.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.exception.ConnectionException;

class ConnectionTypeRequestProcessor implements ConnectionRequestProcessor {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        ConnectionMessageType messageType;
        try {
            int connectionType = is.read();
            if (connectionType == -1) {
                throw new IOException("No data available for reading CD value");
            }
            messageType = ConnectionMessageType.fromByte((byte) connectionType);
            if (messageType == null) {
                throw new IllegalArgumentException("Incorrect value for CD: %d".formatted(connectionType));
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during hello message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withMessageType(messageType);
    }
}
