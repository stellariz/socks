package ru.stellariz.socks.socks4.connection.handlers;

import ru.stellariz.socks.common.OnFailedConnectionHandler;
import ru.stellariz.socks.common.utils.socks4.ConnectionMessageResponse;

public class ConnectionHandlerOnFailureHandler extends OnFailedConnectionHandler {

    @Override
    protected byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, ConnectionMessageResponse.REQUEST_REJECTED.getValue(), 0, 0};
    }
}
