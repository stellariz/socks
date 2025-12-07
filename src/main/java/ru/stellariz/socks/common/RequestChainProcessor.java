package ru.stellariz.socks.common;

import java.io.InputStream;
import java.util.List;

/**
 * Композитный обрабочтик сообщения от клиента.
 * Возвращает контекст запроса в виде объекта типа U
 * @param <T> билдер контекста
 * @param <U> контекст запроса подключения
 */
public abstract class RequestChainProcessor<T, U> {
    /**
     * Каждый элемент списка выполняет чтение запроса клиента и получает необходимые данные
     * (версия протокола, операция и тд)
     */
    protected final List<ConnectionRequestProcessor<T>> requestProcessors;

    public RequestChainProcessor(List<ConnectionRequestProcessor<T>> requestProcessors) {
        this.requestProcessors = requestProcessors;
    }

    public abstract U buildContextFromClientRequest(InputStream is);
}
