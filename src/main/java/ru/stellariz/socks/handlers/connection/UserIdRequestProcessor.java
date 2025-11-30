package ru.stellariz.socks.handlers.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import ru.stellariz.socks.dto.ConnectionRequestContext;
import ru.stellariz.socks.exception.ConnectionException;

class UserIdRequestProcessor implements ConnectionRequestProcessor{

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] userId;
        try {
            int b;
            while ((b = is.read()) != -1) {
                // Заканчивается запрос нулевым байтом
                if (b == 0) {
                    break;
                }
                byteArrayOutputStream.write(b);
            }
            userId = byteArrayOutputStream.toByteArray();
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during hello message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withUserId(userId.length > 0 ? new String(userId) : UUID.randomUUID().toString());
    }
}
