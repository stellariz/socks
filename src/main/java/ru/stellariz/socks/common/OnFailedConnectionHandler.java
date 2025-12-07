package ru.stellariz.socks.common;

import java.io.IOException;
import java.io.OutputStream;

public abstract class OnFailedConnectionHandler {
    /**
     * Отправляет сообщения клиенту о неудачном соединении
     *
     * @param outputStream
     * @throws IOException
     */
    public void sendFailedConnectionMessage(OutputStream outputStream) throws IOException {
        outputStream.write(createFailedConnectionEstablishedMessage());
    }

    /**
     * @return сообщение для клиента
     */
    protected abstract byte[] createFailedConnectionEstablishedMessage();
}
