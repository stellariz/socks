package ru.stellariz.socks.socks4.connection.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;
import ru.stellariz.socks.common.utils.ConnectionMessageResponse;

public class ConnectionHandlerOnFailureHandler implements ConnectionTypeHandler<ConnectionRequestContext> {

    @Override
    public Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException {
        os.write(createFailedConnectionEstablishedMessage());
        return null;
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, ConnectionMessageResponse.REQUEST_REJECTED.getValue(), 0, 0};
    }
}
