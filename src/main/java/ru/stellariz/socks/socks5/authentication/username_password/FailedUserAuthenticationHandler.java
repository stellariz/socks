package ru.stellariz.socks.socks5.authentication.username_password;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.socks5.ConnectionMessageResponse;
import ru.stellariz.socks.socks5.authentication.UserAuthenticationHandler;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;

public class FailedUserAuthenticationHandler implements UserAuthenticationHandler {
    @Override
    public void notifyUser(OutputStream os, UsernamePasswordAuthenticationContext context) throws IOException {
        os.write(createFailedAuthenticationMessage(context));
    }

    private byte[] createFailedAuthenticationMessage(UsernamePasswordAuthenticationContext context) {
        return new byte[]{
                context.negotiationVersion().getNegotiationVersion(),
                ConnectionMessageResponse.REQUEST_REJECTED.getValue()
        };
    }
}
