package ru.stellariz.socks.socks5.authentication.username_password;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.RequestChainProcessor;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;

public class UsernamePasswordAuthenticationChainProcessor extends
        RequestChainProcessor<UsernamePasswordAuthenticationContext.UsernamePasswordAuthenticationContextBuilder, UsernamePasswordAuthenticationContext> {

    public UsernamePasswordAuthenticationChainProcessor() {
        super(
                List.of(
                        new NegotiationVersionRequestProcessor(),
                        new UsernameRequestProcessor(),
                        new PasswordRequestProcessor()
                )
        );
    }

    @Override
    public UsernamePasswordAuthenticationContext buildContextFromClientRequest(InputStream is) {
        var contextBuilder = UsernamePasswordAuthenticationContext.builder();
        for (var processor : requestProcessors) {
            contextBuilder = processor.processRequest(contextBuilder, is);
            if (contextBuilder.getException() != null) {
                break;
            }
        }
        return contextBuilder.build();
    }
}
