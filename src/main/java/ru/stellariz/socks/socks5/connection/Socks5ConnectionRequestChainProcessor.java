package ru.stellariz.socks.socks5.connection;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.common.RequestChainProcessor;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

public class Socks5ConnectionRequestChainProcessor extends
        RequestChainProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder, ConnectionRequestContext> {

    /**
     * The client connects to the server, and sends a version
     * identifier/method selection message:
     * <p>
     * +----+----------+----------+
     * |VER | NMETHODS | METHODS  |
     * +----+----------+----------+
     * | 1  |    1     | 1 to 255 |
     * +----+----------+----------+
     * <p>
     * Once the method-dependent subnegotiation has completed, the client
     * sends the request details.  If the negotiated method includes
     * encapsulation for purposes of integrity checking and/or
     * confidentiality, these requests MUST be encapsulated in the method-dependent encapsulation.
     * <p>
     * The SOCKS request is formed as follows:
     * <p>
     * +----+-----+-------+------+----------+----------+
     * |VER | CMD |  RSV  | ATYP | DST.ADDR | DST.PORT |
     * +----+-----+-------+------+----------+----------+
     * | 1  |  1  | X'00' |  1   | Variable |    2     |
     * +----+-----+-------+------+----------+----------+
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
