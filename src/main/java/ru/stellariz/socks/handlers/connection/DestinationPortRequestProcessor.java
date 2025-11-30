package ru.stellariz.socks.handlers.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.exception.ConnectionException;
import ru.stellariz.socks.utils.ByteConversionUtils;

class DestinationPortRequestProcessor implements ConnectionRequestProcessor {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        byte[] dstPort = new byte[2];
        try {
            int totalRead = is.read(dstPort);
            if (totalRead != 2) {
                throw new IOException("Invalid data read for destination port");
            }
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during hello message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withDstPort(ByteConversionUtils.convertToPortNumber(dstPort[0], dstPort[1]));
    }
}
