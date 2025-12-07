package ru.stellariz.socks.socks5.connection.handlers.authentication;

import java.io.IOException;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationMethod;

/**
 * Обработчик для выбора типа аутентификации и нотификации клиента
 *
 * @param <T> тип контекста
 */
public interface AuthenticationTypeHandler<T> {
    AuthenticationMethod choseMethodAndNotifyClient(T context, OutputStream clientOs) throws IOException;
}
