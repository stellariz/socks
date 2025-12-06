package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.authentication.AuthenticationMethod;
import ru.stellariz.socks.socks5.context.AuthenticationContext;

/**
 * TODO(r.popov): change base interface for authentication handlers
 */
public class AuthenticationHandlerOnSucceedHandler implements AuthenticationTypeHandler<AuthenticationContext> {

    @Override
    public AuthenticationMethod choseMethodAndNotifyClient(AuthenticationContext context,
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
