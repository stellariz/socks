package ru.stellariz.socks.handlers.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.exception.ConnectionException;

class DestinationIPRequestProcessor implements ConnectionRequestProcessor {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        byte[] dstIp = new byte[4];
        try {
            int totalRead = is.read(dstIp);
            if (totalRead != 4) {
                throw new IOException("Invalid data read for destination ip");
            }
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during reading destination ip from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withDstAddress(dstIp);
    }
}
