package ru.stellariz.socks.socks4.context;

import java.net.InetAddress;
import ru.stellariz.socks.common.utils.ConnectionMessageType;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.utils.SocksVersion;

public record ConnectionRequestContext(
        SocksVersion socksVersion,
        ConnectionMessageType messageType,
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
                messageType,
                dstPort,
                resolvedDstAddress.getAddress(),
                resolvedDstAddress, userId, exception
        );
    }

    public static class ConnectionRequestContextBuilder {
        private SocksVersion socksVersion;
        private ConnectionMessageType messageType;
        private int dstPort;
        private byte[] dstAddress;
        private InetAddress resolvedDstAddress;
        private String userId;
        private ConnectionException exception;

        private ConnectionRequestContextBuilder() {
        }

        private ConnectionRequestContextBuilder(SocksVersion socksVersion, ConnectionMessageType messageType,
                                                int dstPort, byte[] dstAddress, InetAddress resolvedDstAddress,
                                                String userId, ConnectionException exception) {
            this.socksVersion = socksVersion;
            this.messageType = messageType;
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

        public ConnectionRequestContextBuilder withMessageType(ConnectionMessageType messageType) {
            this.messageType = messageType;
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
                    socksVersion, messageType, dstPort, resolvedDstAddress, userId, exception
            );
        }
    }
}
