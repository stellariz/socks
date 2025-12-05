package ru.stellariz.socks.socks4.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

class DestinationIPRequestProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

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
