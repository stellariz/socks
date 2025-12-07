package ru.stellariz.socks.socks5.connection.authentication;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.RequestChainProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.context.ConnectionAuthenticationContext;

public class MethodAuthenticationSelector
        extends RequestChainProcessor<ConnectionAuthenticationContext.AuthenticationContextBuilder, ConnectionAuthenticationContext> {


    public MethodAuthenticationSelector() {
        super(
                List.of(
                        new SocksVersionRequestProcessor(SocksVersion.SOCKS_5),
                        new MethodsNumberRequestProcessor(),
                        new AuthenticationMethodsRequestProcessor()
                ));
    }


    @Override
    public ConnectionAuthenticationContext buildContextFromClientRequest(InputStream is) {
        var contextBuilder = ConnectionAuthenticationContext.builder();
        for (var processor : requestProcessors) {
            contextBuilder = processor.processRequest(contextBuilder, is);
            if (contextBuilder.getException() != null) {
                break;
            }
        }
        return contextBuilder.build();
    }
}
