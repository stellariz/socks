package ru.stellariz.socks.handlers.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.exception.ConnectionException;
import ru.stellariz.socks.utils.SocksVersion;

/**
 * Обработчик версии протокола Socks
 */
class SocksVersionRequestProcessor implements ConnectionRequestProcessor {

    private final SocksVersion socksVersionProtocol;

    public SocksVersionRequestProcessor(SocksVersion socksVersion) {
        this.socksVersionProtocol = socksVersion;
    }

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        try {
            int socksVersion = is.read();
            if (socksVersion == -1) {
                throw new IOException("No data available for reading SOCKS version");
            }
            if ((byte)socksVersion != socksVersionProtocol.getProtocolVersion()) {
                throw new IllegalArgumentException("Unsupported protocol version");
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during hello message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withProtocolVersion(socksVersionProtocol);
    }
}
