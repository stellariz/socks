package ru.stellariz.socks.handlers.connection;

import java.io.InputStream;
import ru.stellariz.socks.dto.ConnectionRequestContext;

/**
 * Обработчик сообщения о подключении от клиента
 * <p>
 * Важно! Обработчик может обрабатывать как CONNECT сообщение так и BIND (не отталкиваться от названия)
 */
interface ConnectionRequestProcessor {

    ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is
    );


}
