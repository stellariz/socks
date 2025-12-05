package ru.stellariz.socks.socks4.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.utils.ByteConversionUtils;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

class DestinationPortRequestProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder>{

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
