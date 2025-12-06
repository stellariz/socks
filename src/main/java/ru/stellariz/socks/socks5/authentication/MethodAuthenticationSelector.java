package ru.stellariz.socks.socks5.authentication;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.ConnectionRequestChainProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.context.AuthenticationContext;

public class MethodAuthenticationSelector
        extends ConnectionRequestChainProcessor<AuthenticationContext.AuthenticationContextBuilder, AuthenticationContext> {


    public MethodAuthenticationSelector() {
        super(
                List.of(
                        new SocksVersionRequestProcessor(SocksVersion.SOCKS_5),
                        new MethodsNumberRequestProcessor(),
                        new AuthenticationMethodsRequestProcessor()
                ));
    }


    @Override
    public AuthenticationContext buildContextFromClientRequest(InputStream is) {
        var contextBuilder = AuthenticationContext.builder();
        for (var processor : requestProcessors) {
            contextBuilder = processor.processRequest(contextBuilder, is);
            if (contextBuilder.getException() != null) {
                break;
            }
        }
        return contextBuilder.build();
    }
}
