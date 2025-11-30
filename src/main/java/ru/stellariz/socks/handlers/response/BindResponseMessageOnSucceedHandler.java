package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;

public class BindResponseMessageOnSucceedHandler implements ConnectionResponseMessageHandler {

    @Override
    public void sendResponse(OutputStream os,  ConnectionRequestContext connectionRequestContext)
            throws IOException {
        os.write(createSucceedBindMessage(connectionRequestContext.dstPort()));
    }

    private byte[] createSucceedBindMessage(int port) {
        return new byte[]{0, (byte) 0x5a, (byte)(port >> 8), (byte)(port & 0xff), 0, 0, 0, 0};
    }
}
