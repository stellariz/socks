package ru.stellariz.socks.socks5.connection.authentication;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.socks5.context.ConnectionAuthenticationContext;
import ru.stellariz.socks.socks5.exception.AuthenticationException;

class MethodsNumberRequestProcessor implements
        ConnectionRequestProcessor<ConnectionAuthenticationContext.AuthenticationContextBuilder> {

    @Override
    public ConnectionAuthenticationContext.AuthenticationContextBuilder
    processRequest(ConnectionAuthenticationContext.AuthenticationContextBuilder contextBuilder, InputStream is) {
        int methodsNumber;
        try {
            methodsNumber = is.read();
            if (methodsNumber == -1) {
                throw new IOException("No data available for reading SOCKS version");
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during authentication message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return contextBuilder.withException(new AuthenticationException(ex));
        }
        return contextBuilder.withMethodsNumber(methodsNumber);
    }
}
