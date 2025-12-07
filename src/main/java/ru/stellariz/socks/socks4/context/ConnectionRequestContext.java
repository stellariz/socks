package ru.stellariz.socks.socks4.context;

import java.net.InetAddress;
import ru.stellariz.socks.common.utils.OperationType;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.utils.SocksVersion;

/**
 * Контекст запроса клиента при установлении соединения
 *
 * @param socksVersion версия протокола SOCKS
 * @param operationType тип соединения
 * @param dstPort порт назначения
 * @param resolvedDstAddress InetAddress хоста (после резолвинга в случае DNS запроса)
 * @param userId userId
 * @param exception ошибка при обработке запроса клиента
 */
public record ConnectionRequestContext(
        SocksVersion socksVersion,
        OperationType operationType,
        int dstPort,
        InetAddress resolvedDstAddress,
        String userId,
        ConnectionException exception
) {

    public static ConnectionRequestContextBuilder builder() {
        return new ConnectionRequestContextBuilder();
    }

    public ConnectionRequestContextBuilder mutate() {
        return new ConnectionRequestContextBuilder(
                socksVersion,
                operationType,
                dstPort,
                resolvedDstAddress.getAddress(),
                resolvedDstAddress, userId, exception
        );
    }

    public static class ConnectionRequestContextBuilder {
        private SocksVersion socksVersion;
        private OperationType operationType;
        private int dstPort;
        private byte[] dstAddress;
        private InetAddress resolvedDstAddress;
        private String userId;
        private ConnectionException exception;

        private ConnectionRequestContextBuilder() {
        }

        private ConnectionRequestContextBuilder(SocksVersion socksVersion, OperationType messageType,
                                                int dstPort, byte[] dstAddress, InetAddress resolvedDstAddress,
                                                String userId, ConnectionException exception) {
            this.socksVersion = socksVersion;
            this.operationType = messageType;
            this.dstPort = dstPort;
            this.dstAddress = dstAddress;
            this.resolvedDstAddress = resolvedDstAddress;
            this.userId = userId;
            this.exception = exception;
        }

        public byte[] getDstAddress() {
            return dstAddress;
        }

        public ConnectionException getException() {
            return exception;
        }


        public ConnectionRequestContextBuilder withProtocolVersion(SocksVersion socksVersion) {
            this.socksVersion = socksVersion;
            return this;
        }

        public ConnectionRequestContextBuilder withDstPort(int dstPort) {
            this.dstPort = dstPort;
            return this;
        }

        public ConnectionRequestContextBuilder withDstAddress(byte[] dstAddress) {
            this.dstAddress = dstAddress;
            return this;
        }

        public ConnectionRequestContextBuilder withResolvedDstAddress(InetAddress resolvedDstAddress) {
            this.resolvedDstAddress = resolvedDstAddress;
            return this;
        }

        public ConnectionRequestContextBuilder withOperationType(OperationType messageType) {
            this.operationType = messageType;
            return this;
        }

        public ConnectionRequestContextBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public ConnectionRequestContextBuilder withException(ConnectionException exception) {
            this.exception = exception;
            return this;
        }

        public ConnectionRequestContext build() {
            return new ConnectionRequestContext(
                    socksVersion, operationType, dstPort, resolvedDstAddress, userId, exception
            );
        }
    }
}
