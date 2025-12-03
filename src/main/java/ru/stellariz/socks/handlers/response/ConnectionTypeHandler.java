package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import ru.stellariz.socks.dto.ConnectionRequestContext;

public interface ConnectionTypeHandler {
    Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException;
}
