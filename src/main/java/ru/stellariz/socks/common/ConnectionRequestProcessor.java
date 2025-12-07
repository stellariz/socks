package ru.stellariz.socks.common;

import java.io.InputStream;

/**
 * Обработчик сообщения о подключении от клиента
 */
public interface ConnectionRequestProcessor<T> {

    T processRequest(T context, InputStream is);
}
