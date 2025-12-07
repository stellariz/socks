package ru.stellariz.socks.socks5.connection.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import ru.stellariz.socks.common.ConnectionTypeHandler;
import ru.stellariz.socks.common.utils.ByteConversionUtils;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.common.utils.socks5.ConnectionDecisionResponse;
import ru.stellariz.socks.socks5.context.AddressType;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

public class SucceedConnectionHandler implements ConnectionTypeHandler<ConnectionRequestContext> {

    @Override
    public Socket createSocket(OutputStream os, ConnectionRequestContext connectionRequestContext) throws IOException {
        InetAddress inetAddress = connectionRequestContext.resolvedDstAddress();
        int port = connectionRequestContext.dstPort();
        Socket destSocket = new Socket(inetAddress, port);
        System.out.printf("Thread [%s]: Connection to %s:%d established\n",
                Thread.currentThread().getName(), inetAddress.getHostName(), port);
        os.write(createSucceedConnectionEstablishedMessage(destSocket));
        return destSocket;
    }

    private byte[] createSucceedConnectionEstablishedMessage(Socket destSocket) {
        int destSocketPort = destSocket.getPort();
        byte[] portInBytes = ByteConversionUtils.convertPortToBytes(destSocketPort);
        return new byte[]{
                SocksVersion.SOCKS_5.getProtocolVersion(),
                ConnectionDecisionResponse.SUCCEED.getValue(),
                0,
                AddressType.IP_V4.getValue(),
                0x7f, 0, 0, 0x01,
                portInBytes[0], portInBytes[1]};
    }

}
