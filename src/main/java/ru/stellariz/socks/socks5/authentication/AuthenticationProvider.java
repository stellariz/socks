package ru.stellariz.socks.socks5.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationResult;

public interface AuthenticationProvider {
    AuthenticationResult authenticate(InputStream is, OutputStream os) throws IOException;
}
