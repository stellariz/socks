package ru.stellariz.socks.socks5.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationResult;

/**
 * Обработчик для аутентифкиации клиента
 * Получает данные и затем нотфицирует клинта об успешности/неуспешности аутентификации
 */
public interface AuthenticationProvider {
    AuthenticationResult authenticate(InputStream is, OutputStream os) throws IOException;
}
