package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationMethod;
import ru.stellariz.socks.socks5.context.ConnectionAuthenticationContext;

public class SucceedAuthenticationHandler implements AuthenticationTypeHandler<ConnectionAuthenticationContext> {

    @Override
    public AuthenticationMethod choseMethodAndNotifyClient(ConnectionAuthenticationContext context,
                                                           OutputStream clientOs) throws IOException {
        var availableAuthMethod =
                AuthenticationMethod.findFirstAvailableAuthenticationMethod(context.clientAuthentication());
        clientOs.write(createSucceedAuthenticationMessage(context.protocolVersion(),availableAuthMethod));
        return availableAuthMethod;
    }

    private byte[] createSucceedAuthenticationMessage(SocksVersion socksVersion,
                                                      AuthenticationMethod authenticationMethod) {
        return new byte[]{socksVersion.getProtocolVersion(), authenticationMethod.getValue()};
    }

}
