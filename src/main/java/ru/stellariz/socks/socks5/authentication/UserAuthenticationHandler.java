package ru.stellariz.socks.socks5.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;

public interface UserAuthenticationHandler {
    void notifyUser(OutputStream os, UsernamePasswordAuthenticationContext context) throws IOException;
}
