package ru.stellariz.socks.handlers.response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.handlers.connection.ConnectionMessageResponse;

public class ConnectionHandlerOnFailureHandler implements ConnectionTypeHandler {

    @Override
    public Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException {
        os.write(createFailedConnectionEstablishedMessage());
        return null;
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, ConnectionMessageResponse.REQUEST_REJECTED.getValue(), 0, 0};
    }
}
