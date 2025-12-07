package ru.stellariz.socks.socks5.authentication.username_password;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.common.utils.socks5.ConnectionDecisionResponse;
import ru.stellariz.socks.socks5.authentication.UserAuthenticationHandler;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;

public class SucceedUserAuthenticationHandler implements UserAuthenticationHandler {

    @Override
    public void notifyUser(OutputStream os, UsernamePasswordAuthenticationContext context) throws IOException {
        os.write(createSucceedAuthenticationMessage(context));
    }

    private byte[] createSucceedAuthenticationMessage(UsernamePasswordAuthenticationContext context) {
        return new byte[]{
                context.negotiationVersion().getNegotiationVersion(),
                ConnectionDecisionResponse.SUCCEED.getValue()};
    }
}
