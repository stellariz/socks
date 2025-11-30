package ru.stellariz.socks.handlers.connection;

import java.io.InputStream;
import java.util.List;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.utils.SocksVersion;

/**
 * Композитный обрабочтик сообщения о подключении клиента
 */
public class ConnectionRequestChainProcessor {
    /**
     * Каждый элемент списка выполняет чтение запроса клиента и получает необходимые данные
     * (версия протокола, операция и тд)
     */
    private final List<ConnectionRequestProcessor> requestProcessors;

    private ConnectionRequestChainProcessor(List<ConnectionRequestProcessor> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    /**
     * TODO(r.popov): describe protocol
     */
    public static ConnectionRequestChainProcessor buildSocks4ConnectionMessageProcessorChain() {
        return new ConnectionRequestChainProcessor(
                List.of(
                        new SocksVersionRequestProcessor(SocksVersion.SOCKS_4),
                        new ConnectionTypeRequestProcessor(),
                        new DestinationPortRequestProcessor(),
                        new DestinationIPRequestProcessor(),
                        new UserIdRequestProcessor(),
                        new DestinationAddressNameRequestProcessor()
                )
        );
    }

    public ConnectionRequestContext processRequest(InputStream is) {
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
