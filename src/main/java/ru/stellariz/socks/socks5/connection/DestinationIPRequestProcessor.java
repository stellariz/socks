package ru.stellariz.socks.socks5.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks5.context.AddressType;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

class DestinationIPRequestProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        InetAddress resolvedInetAddress;
        try {
            switch (context.getAddressType()) {
                case IP_V4 -> resolvedInetAddress = readIPV4(is);
                case IP_V6 ->  resolvedInetAddress = readIPV6(is);
                case DOMAIN -> resolvedInetAddress = readHostName(is);
                case null, default -> throw new IOException("Unknown address type");
            }
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error during reading destination ip from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withResolvedDstAddress(resolvedInetAddress);
    }

    private InetAddress readIPV4(InputStream is) throws IOException {
        byte[] dstIp = new byte[AddressType.IP_V4.getBytesLength()];
        int totalRead = is.read(dstIp);
        if (totalRead != AddressType.IP_V4.getBytesLength()) {
            throw new IOException("Invalid data read for destination ip");
        }
        return InetAddress.getByAddress(dstIp);
    }

    private InetAddress readIPV6(InputStream is) throws IOException {
        byte[] dstIp = new byte[AddressType.IP_V6.getBytesLength()];
        int totalRead = is.read(dstIp);
        if (totalRead != AddressType.IP_V6.getBytesLength()) {
            throw new IOException("Invalid data read for destination ip");
        }
        return InetAddress.getByAddress(dstIp);
    }

    private InetAddress readHostName(InputStream is) throws IOException {
        int hostNameLength = is.read();
        if (hostNameLength <= 0) {
            throw new IOException("Invalid amount of bytes was read for host name");
        }
        byte[] hostName = new byte[hostNameLength];
        int totalRead = is.read(hostName);
        if (totalRead != hostNameLength) {
            throw new IOException("Invalid data read for host name");
        }
        return InetAddress.getByName(new String(hostName));
    }
}
