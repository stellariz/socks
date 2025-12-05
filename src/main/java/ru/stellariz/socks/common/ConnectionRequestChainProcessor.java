package ru.stellariz.socks.common;

import java.io.InputStream;
import java.util.List;

/**
 * Композитный обрабочтик сообщения о подключении клиента
 *
 * T - context builder
 * U - context
 */
public abstract class ConnectionRequestChainProcessor<T, U> {
    /**
     * Каждый элемент списка выполняет чтение запроса клиента и получает необходимые данные
     * (версия протокола, операция и тд)
     */
    protected final List<ConnectionRequestProcessor<T>> requestProcessors;

    public ConnectionRequestChainProcessor(List<ConnectionRequestProcessor<T>> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    public abstract U buildContextFromClientRequest(InputStream is);
}
