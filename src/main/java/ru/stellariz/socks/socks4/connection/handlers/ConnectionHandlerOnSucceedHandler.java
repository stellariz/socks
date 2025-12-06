package ru.stellariz.socks.socks4.connection.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;
import ru.stellariz.socks.common.utils.socks4.ConnectionMessageResponse;

public class ConnectionHandlerOnSucceedHandler implements ConnectionTypeHandler<ConnectionRequestContext> {

    @Override
    public Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException {
        InetAddress inetAddress = connectionRequestContext.resolvedDstAddress();
        int port = connectionRequestContext.dstPort();
        Socket destSocket = new Socket(inetAddress, port);
        System.out.printf("Thread [%s]: Connection to %s:%d established\n",
                Thread.currentThread().getName(), inetAddress.getHostName(), port);
        os.write(createSucceedConnectionEstablishedMessage());
        return destSocket;
    }

    private byte[] createSucceedConnectionEstablishedMessage() {
        // last 6 bytes are ignored by client
        return new byte[]{0, ConnectionMessageResponse.REQUEST_GRANTED.getValue(), 0, 0, 0, 0, 0, 0};
    }

}
