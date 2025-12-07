package ru.stellariz.socks.socks5.authentication.username_password;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.utils.NegotiationVersion;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;
import ru.stellariz.socks.socks5.exception.UsernamePasswordAuthenticationException;


class NegotiationVersionRequestProcessor implements
        ConnectionRequestProcessor<UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder> {

    @Override
    public UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder processRequest(
            UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder context, InputStream is) {
        NegotiationVersion negotiationVersion;
        try {
            byte ver = (byte) is.read();
            if (ver == -1) {
                throw new IOException("No data available for reading negotiation version");
            }
            negotiationVersion = NegotiationVersion.fromByte(ver);
            if (negotiationVersion == null) {
                return context.withException(new UsernamePasswordAuthenticationException("Unknown negotiation version"));
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during client authentication: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new UsernamePasswordAuthenticationException(ex));
        }
        return context.withNegotiationVersion(negotiationVersion);
    }
}
