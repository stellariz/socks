package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationMethod;
import ru.stellariz.socks.socks5.context.ConnectionAuthenticationContext;

public class FailedAuthenticationHandler implements AuthenticationTypeHandler<ConnectionAuthenticationContext> {

    @Override
    public AuthenticationMethod choseMethodAndNotifyClient(ConnectionAuthenticationContext context, OutputStream clientOs) throws IOException {
        clientOs.write(createFailedAuthenticationMessage());
        return AuthenticationMethod.NO_AVAILABLE;
    }

    private byte[] createFailedAuthenticationMessage() {
        return new byte[]{SocksVersion.SOCKS_5.getProtocolVersion(), AuthenticationMethod.NO_AVAILABLE.getValue()};
    }

}
