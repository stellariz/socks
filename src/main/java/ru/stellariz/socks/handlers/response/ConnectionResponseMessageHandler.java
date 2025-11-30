package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;

public interface ConnectionResponseMessageHandler {
    void sendResponse(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException;
}
