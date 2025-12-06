package ru.stellariz.socks.socks5.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.socks5.context.AuthenticationContext;
import ru.stellariz.socks.socks5.exception.AuthenticationException;

class AuthenticationMethodsRequestProcessor implements
        ConnectionRequestProcessor<AuthenticationContext.AuthenticationContextBuilder> {


    @Override
    public AuthenticationContext.AuthenticationContextBuilder processRequest(
            AuthenticationContext.AuthenticationContextBuilder context, InputStream is) {
        List<AuthenticationMethod> clientMethods = new ArrayList<>();
        try {
            for (int i = 0; i < context.getMethodsNumber(); ++i) {
                byte method = (byte) is.read();
                AuthenticationMethod authMethod = AuthenticationMethod.fromByte(method);
                if (authMethod != null) {
                    clientMethods.add(authMethod);
                }
            }
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during authentication message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new AuthenticationException(ex));
        }
        return context.withClientAuthentication(clientMethods);
    }
}
