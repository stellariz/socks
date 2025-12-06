package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.authentication.AuthenticationMethod;

public interface AuthenticationTypeHandler<T> {
    AuthenticationMethod choseMethodAndNotifyClient(T context, OutputStream clientOs) throws IOException;
}
