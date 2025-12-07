package ru.stellariz.socks.socks5.connection;

import java.io.IOException;
import java.io.InputStream;
import ru.stellariz.socks.common.ConnectionRequestProcessor;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.socks5.context.AddressType;
import ru.stellariz.socks.socks5.context.ConnectionRequestContext;

/**
 * Обработчик типа IP адреса хоста для подключения
 */
class AddressTypeProcessor implements
        ConnectionRequestProcessor<ConnectionRequestContext.ConnectionRequestContextBuilder> {

    @Override
    public ConnectionRequestContext.ConnectionRequestContextBuilder processRequest(
            ConnectionRequestContext.ConnectionRequestContextBuilder context, InputStream is) {
        AddressType addressType;
        try {
            byte addrType = (byte) is.read();
            if (addrType == -1) {
                throw new IOException("No data available for reading ATYP value");
            }
            addressType = AddressType.fromByte(addrType);
            if (addressType == null) {
                throw new IllegalArgumentException("Incorrect value for ATYP: %d".formatted(addrType));
            }
        } catch (IllegalArgumentException | IOException ex) {
            System.err.printf("Thread [%s]: Error during hello message from client: [%s]\n",
                    Thread.currentThread().getName(), ex.getMessage());
            return context.withException(new ConnectionException(ex));
        }
        return context.withAddressType(addressType);
    }
}
