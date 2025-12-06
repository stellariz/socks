package ru.stellariz.socks.socks5.connection.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.common.utils.ByteConversionUtils;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.common.utils.socks5.ConnectionMessageResponse;
import ru.stellariz.socks.socks5.context.AddressType;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

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
        return new byte[]{
                SocksVersion.SOCKS_5.getProtocolVersion(),
                ConnectionMessageResponse.SUCCEED.getValue(),
                0,
                AddressType.IP_V4.getValue(),
                0, 0, 0, 0,
                portInBytes[0], portInBytes[1]};
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{
                SocksVersion.SOCKS_5.getProtocolVersion(),
                ConnectionMessageResponse.REQUEST_REJECTED.getValue(),
                0,
                AddressType.IP_V4.getValue(),
                0, 0, 0, 0,
                0, 0};
    }
}
