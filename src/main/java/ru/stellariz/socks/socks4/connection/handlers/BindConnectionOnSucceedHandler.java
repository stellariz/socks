package ru.stellariz.socks.socks4.connection.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.common.utils.ByteConversionUtils;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;
import ru.stellariz.socks.common.utils.ConnectionMessageResponse;

public class BindConnectionOnSucceedHandler implements ConnectionTypeHandler<ConnectionRequestContext> {
    private static final int ANY_PORT = 0;

    @Override
    public Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext)
            throws IOException {
        try (var additionalServer = new ServerSocket(ANY_PORT)) {
            System.out.printf("Thread [%s]: Connection for incoming requests from [%s] is opened on port: [%d]\n",
                    Thread.currentThread().getName(),
                    connectionRequestContext.resolvedDstAddress(),
                    additionalServer.getLocalPort());
            os.write(createSucceedBindMessage(additionalServer.getLocalPort()));
            var destSocket =  additionalServer.accept();
            if (!destSocket.getInetAddress().equals(connectionRequestContext.resolvedDstAddress()) &&
                    !doesClientSendAnyAddressAndServerIsLoopback(connectionRequestContext, destSocket)) {
                os.write(createFailedConnectionEstablishedMessage());
                destSocket.close();
                throw new ConnectException("Unequal IP addresses for binding request");
            }

            os.write(createSucceedBindMessage(additionalServer.getLocalPort()));
            return destSocket;
        }
    }

    private static boolean doesClientSendAnyAddressAndServerIsLoopback(ConnectionRequestContext connectionRequestContext,
                                                                       Socket destSocket) {
        return destSocket.getInetAddress().isLoopbackAddress() &&
                connectionRequestContext.resolvedDstAddress().isAnyLocalAddress();
    }

    private byte[] createSucceedBindMessage(int port) {
        byte[] portInBytes = ByteConversionUtils.convertPortToBytes(port);
        return new byte[]{0, ConnectionMessageResponse.REQUEST_GRANTED.getValue(), portInBytes[0], portInBytes[1], 0, 0, 0, 0};
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, ConnectionMessageResponse.REQUEST_REJECTED.getValue(), 0, 0};
    }
}
