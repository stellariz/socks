package ru.stellariz.socks.socks5.connection.handlers;

import ru.stellariz.socks.common.OnFailedConnectionHandler;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.common.utils.socks5.ConnectionDecisionResponse;
import ru.stellariz.socks.socks5.context.AddressType;

public class FailedConnectionHandler extends OnFailedConnectionHandler {

    @Override
    protected byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{
                SocksVersion.SOCKS_5.getProtocolVersion(),
                ConnectionDecisionResponse.REQUEST_REJECTED.getValue(),
                0,
                AddressType.IP_V4.getValue(),
                0, 0, 0, 0,
                0, 0};
    }
}
