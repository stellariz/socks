package ru.stellariz.socks.common;

import java.io.IOException;
import java.io.OutputStream;

public abstract class OnFailedConnectionHandler {

    public void sendFailedConnectionMessage(OutputStream outputStream) throws IOException {
        outputStream.write(createFailedConnectionEstablishedMessage());
    }

    protected abstract byte[] createFailedConnectionEstablishedMessage();
}
