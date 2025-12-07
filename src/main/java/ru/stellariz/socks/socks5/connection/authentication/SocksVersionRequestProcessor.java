package ru.stellariz.socks.socks5.connection.authentication;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.context.ConnectionAuthenticationContext;
import ru.stellariz.socks.socks5.exception.AuthenticationException;

/**
 * Обработчик версии протокола Socks
 */
class SocksVersionRequestProcessor implements
        ConnectionRequestProcessor<ConnectionAuthenticationContext.AuthenticationContextBuilder> {

    private final SocksVersion socksVersionProtocol;

    public SocksVersionRequestProcessor(SocksVersion socksVersion) {
        this.socksVersionProtocol = socksVersion;
    }

    @Override
    public ConnectionAuthenticationContext.AuthenticationContextBuilder processRequest(
            ConnectionAuthenticationContext.AuthenticationContextBuilder context, InputStream is) {
        try {
            byte socksVersion = (byte) is.read();
            if (socksVersion == -1) {
                throw new IOException("No data available for reading SOCKS version");
            }
            if (socksVersion != socksVersionProtocol.getProtocolVersion()) {
                throw new IllegalArgumentException("Unsupported protocol version");
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during authentication message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new AuthenticationException(ex));
        }
        return context.withProtocolVersion(socksVersionProtocol);
    }
}
