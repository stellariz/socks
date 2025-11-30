package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;

public class ConnectionResponseMessageOnFailureHandler implements ConnectionResponseMessageHandler {
    @Override
    public void sendResponse(OutputStream os,  ConnectionRequestContext connectionRequestContext) throws IOException {
        os.write(createFailedConnectionEstablishedMessage());
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, (byte) 0x5b, 0, 0};
    }
}
