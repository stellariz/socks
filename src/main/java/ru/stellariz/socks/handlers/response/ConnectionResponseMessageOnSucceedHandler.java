package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;

public class ConnectionResponseMessageOnSucceedHandler implements ConnectionResponseMessageHandler {

    @Override
    public void sendResponse(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException {
        os.write(createSucceedConnectionEstablishedMessage());
    }

    private byte[] createSucceedConnectionEstablishedMessage() {
        // last 6 bytes are ignored by client
        return new byte[]{0, (byte) 0x5a, 0, 0, 0, 0, 0, 0};
    }

}
