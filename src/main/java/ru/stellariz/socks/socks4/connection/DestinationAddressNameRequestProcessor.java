package ru.stellariz.socks.socks4.connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks4.context.ConnectionRequestContext;

/**
 * Обработчик для получения итогового IP адреса хоста (клиент может передавать доменное имя вместо IP)
 */
class DestinationAddressNameRequestProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        InetAddress resolvedByNameAddress;
        try {
            if (!isDnsRequest(context.getDstAddress())) {
                return context.withResolvedDstAddress(InetAddress.getByAddress(context.getDstAddress()));
            }
            String destinationAddress = readDestinationAddress(is);
            resolvedByNameAddress = InetAddress.getByName(destinationAddress);
        } catch (IOException ex) {
            System.err.printf("Thread [%s]: Error occurred during reading destination ip: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withResolvedDstAddress(resolvedByNameAddress);
    }

    private static boolean isDnsRequest(byte[] dstIp) {
        return dstIp[0] == 0 && dstIp[1] == 0 && dstIp[2] == 0 && dstIp[3] != 0;
    }

    private static String readDestinationAddress(InputStream is) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int b;
        while ((b = is.read()) != -1) {
            if (b == 0) {
                break;
            }
            byteArrayOutputStream.write(b);
        }
        return byteArrayOutputStream.toString();
    }
}
