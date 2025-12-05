package ru.stellariz.socks.socks4.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.utils.ConnectionMessageType;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

class ConnectionTypeRequestProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        ConnectionMessageType messageType;
        try {
            byte connectionType = (byte) is.read();
            if (connectionType == -1) {
                throw new IOException("No data available for reading CD value");
            }
            messageType = ConnectionMessageType.fromByte(connectionType);
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
