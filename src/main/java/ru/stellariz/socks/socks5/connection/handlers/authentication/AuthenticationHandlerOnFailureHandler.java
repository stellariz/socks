package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.authentication.AuthenticationMethod;
import ru.stellariz.socks.socks5.context.AuthenticationContext;

public class AuthenticationHandlerOnFailureHandler implements AuthenticationTypeHandler<AuthenticationContext> {

    @Override
    public AuthenticationMethod choseMethodAndNotifyClient(AuthenticationContext context, OutputStream clientOs) throws IOException {
        clientOs.write(createFailedAuthenticationMessage());
        return AuthenticationMethod.NO_AVAILABLE;
    }

    private byte[] createFailedAuthenticationMessage() {
        return new byte[]{SocksVersion.SOCKS_5.getProtocolVersion(), AuthenticationMethod.NO_AVAILABLE.getValue()};
    }

}
