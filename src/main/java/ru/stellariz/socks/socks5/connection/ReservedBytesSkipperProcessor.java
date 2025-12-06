package ru.stellariz.socks.socks5.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

class ReservedBytesSkipperProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

    private final int skipBytesNumber;

    ReservedBytesSkipperProcessor(int skipBytesNumber) {
        this.skipBytesNumber = skipBytesNumber;
    }

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        try {
            is.skipNBytes(skipBytesNumber);
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during reading skipped bytes from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context;
    }
}
