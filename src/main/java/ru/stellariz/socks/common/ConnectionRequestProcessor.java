package ru.stellariz.socks.common;

import java.io.InputStream;

/**
 * Обработчик сообщения о подключении от клиента
 * <p>
 * Важно! Обработчик может обрабатывать как CONNECT сообщение так и BIND (не отталкиваться от названия)
 */
public interface ConnectionRequestProcessor<T> {

    T processRequest(T context, InputStream is);
}
