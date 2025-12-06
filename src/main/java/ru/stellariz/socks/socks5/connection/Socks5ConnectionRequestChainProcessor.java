package ru.stellariz.socks.socks5.connection;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.ConnectionRequestChainProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

public class Socks5ConnectionRequestChainProcessor extends
        ConnectionRequestChainProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder, ConnectionRequestContext> {

    /**
     * TODO(r.popov): describe protocol
     */
    public Socks5ConnectionRequestChainProcessor() {
        super(
                List.of(
                        new SocksVersionRequestProcessor(SocksVersion.SOCKS_5),
                        new ConnectionTypeRequestProcessor(),
                        new ReservedBytesSkipperProcessor(1),
                        new AddressTypeProcessor(),
                        new DestinationIPRequestProcessor(),
                        new DestinationPortRequestProcessor()
                )
        );
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
