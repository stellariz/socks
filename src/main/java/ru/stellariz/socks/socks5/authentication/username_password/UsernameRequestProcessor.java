package ru.stellariz.socks.socks5.authentication.username_password;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;
import ru.stellariz.socks.socks5.exception.UsernamePasswordAuthenticationException;

class UsernameRequestProcessor implements
        ConnectionRequestProcessor<UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder> {

    @Override
    public UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder processRequest(
            UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder context, InputStream is) {
        String username;
        try {
            int usernameLength = is.read();
            if (usernameLength <= 0) {
                return context.withException(new UsernamePasswordAuthenticationException("Non positive length of username"));
            }
            var usernameBytes = new byte[usernameLength];
            int read = is.read(usernameBytes);
            if (read != usernameLength) {
                return context.withException(new UsernamePasswordAuthenticationException("Not all bytes read for username"));
            }
            username = new String(usernameBytes);
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during reading username data: [%s]",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new UsernamePasswordAuthenticationException(ex));
        }
        return context.withUsername(username);
    }
}
