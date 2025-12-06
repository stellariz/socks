package ru.stellariz.socks.common;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Обработчик различных типов подключений
 * @param <T> - контекст подключения
 */
public interface ConnectionTypeHandler<T> {

    Socket createSocket(OutputStream os, T connectionRequestContext) throws IOException;
}
