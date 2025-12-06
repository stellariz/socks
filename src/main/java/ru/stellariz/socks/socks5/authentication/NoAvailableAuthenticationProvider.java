package ru.stellariz.socks.socks5.authentication;

import java.io.InputStream;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationResult;

public class NoAvailableAuthenticationProvider implements AuthenticationProvider {

    @Override
    public AuthenticationResult authenticate(InputStream is, OutputStream os) {
        return AuthenticationResult.FAILED;
    }
}
