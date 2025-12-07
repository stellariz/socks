package ru.stellariz.socks.socks4.connection.handlers;

import ru.stellariz.socks.common.OnFailedConnectionHandler;
import ru.stellariz.socks.common.utils.socks4.ConnectionDecisionResponse;

public class FailedConnectionHandler extends OnFailedConnectionHandler {

    @Override
    protected byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, ConnectionDecisionResponse.REQUEST_REJECTED.getValue(), 0, 0};
    }
}
