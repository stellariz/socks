package ru.stellariz.socks.socks5.context;

import java.net.InetAddress;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.utils.OperationType;
import ru.stellariz.socks.common.utils.SocksVersion;

/**
 * Контекст запроса клиента при установлении соединения
 *
 * @param socksVersion версия протокола SOCKS
 * @param operationType тип соединения
 * @param addressType тип IP адреса
 * @param resolvedDstAddress InetAddress хоста (после резолвинга в случае DNS запроса)
 * @param dstPort порт хоста
 * @param exception ошибка при подключении
 */
public record ConnectionRequestContext(
        SocksVersion socksVersion,
        OperationType operationType,
        AddressType addressType,
        InetAddress resolvedDstAddress,
        int dstPort,
        ConnectionException exception
) {
    public static ConnectionRequestContextBuilder builder() {
        return new ConnectionRequestContextBuilder();
    }

    public ConnectionRequestContextBuilder mutate() {
        return new ConnectionRequestContextBuilder(
                socksVersion,
                operationType,
                addressType,
                dstPort,
                resolvedDstAddress,
                exception
        );
    }

    public static class ConnectionRequestContextBuilder {
        private SocksVersion socksVersion;
        private OperationType operationType;
        private AddressType addressType;
        private int dstPort;
        private InetAddress resolvedDstAddress;
        private ConnectionException exception;

        private ConnectionRequestContextBuilder() {
        }

        private ConnectionRequestContextBuilder(SocksVersion socksVersion, OperationType operationType,
                                                AddressType addressType, int dstPort,
                                                InetAddress resolvedDstAddress, ConnectionException exception) {
            this.socksVersion = socksVersion;
            this.operationType = operationType;
            this.addressType = addressType;
            this.dstPort = dstPort;
            this.resolvedDstAddress = resolvedDstAddress;
            this.exception = exception;
        }

        public ConnectionException getException() {
            return exception;
        }

        public AddressType getAddressType() {
            return addressType;
        }

        public ConnectionRequestContextBuilder withProtocolVersion(SocksVersion socksVersion) {
            this.socksVersion = socksVersion;
            return this;
        }

        public ConnectionRequestContextBuilder withAddressType(AddressType addressType) {
            this.addressType = addressType;
            return this;
        }

        public ConnectionRequestContextBuilder withDstPort(int dstPort) {
            this.dstPort = dstPort;
            return this;
        }

        public ConnectionRequestContextBuilder withResolvedDstAddress(InetAddress resolvedDstAddress) {
            this.resolvedDstAddress = resolvedDstAddress;
            return this;
        }

        public ConnectionRequestContextBuilder withOperationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }


        public ConnectionRequestContextBuilder withException(ConnectionException exception) {
            this.exception = exception;
            return this;
        }

        public ConnectionRequestContext build() {
            return new ConnectionRequestContext(socksVersion, operationType, addressType, resolvedDstAddress, dstPort, exception);
        }
    }
}
