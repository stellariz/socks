package ru.stellariz.socks.socks4.connection;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.ConnectionRequestChainProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

public class Socks4ConnectionRequestChainProcessor extends
        ConnectionRequestChainProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder, ConnectionRequestContext> {

    /**
     * TODO(r.popov): describe protocol
     */
    public Socks4ConnectionRequestChainProcessor() {
        super(List.of(
                new SocksVersionRequestProcessor(SocksVersion.SOCKS_4),
                new ConnectionTypeRequestProcessor(),
                new DestinationPortRequestProcessor(),
                new DestinationIPRequestProcessor(),
                new UserIdRequestProcessor(),
                new DestinationAddressNameRequestProcessor()
        ));
    }

    @Override
    public ConnectionRequestContext buildContextFromClientRequest(InputStream is) {
        var contextBuilder = ConnectionRequestContext.builder();
        for (var processor : requestProcessors) {
            contextBuilder = processor.processRequest(contextBuilder, is);
            if (contextBuilder.getException() != null) {
                break;
            }
        }
        return contextBuilder.build();
    }
}
