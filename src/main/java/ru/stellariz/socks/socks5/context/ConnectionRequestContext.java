package ru.stellariz.socks.socks5.context;

import java.net.InetAddress;
import ru.stellariz.socks.common.exception.ConnectionException;
import ru.stellariz.socks.common.utils.ConnectionMessageType;
import ru.stellariz.socks.common.utils.SocksVersion;

public record ConnectionRequestContext(
        SocksVersion socksVersion,
        ConnectionMessageType messageType,
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
                messageType,
                addressType,
                dstPort,
                resolvedDstAddress,
                exception
        );
    }

    public static class ConnectionRequestContextBuilder {
        private SocksVersion socksVersion;
        private ConnectionMessageType messageType;
        private AddressType addressType;
        private int dstPort;
        private InetAddress resolvedDstAddress;
        private ConnectionException exception;

        private ConnectionRequestContextBuilder() {
        }

        private ConnectionRequestContextBuilder(SocksVersion socksVersion, ConnectionMessageType messageType,
                                                AddressType addressType, int dstPort,
                                                InetAddress resolvedDstAddress, ConnectionException exception) {
            this.socksVersion = socksVersion;
            this.messageType = messageType;
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

        public ConnectionRequestContextBuilder withMessageType(ConnectionMessageType messageType) {
            this.messageType = messageType;
            return this;
        }


        public ConnectionRequestContextBuilder withException(ConnectionException exception) {
            this.exception = exception;
            return this;
        }

        public ConnectionRequestContext build() {
            return new ConnectionRequestContext(socksVersion, messageType, addressType, resolvedDstAddress, dstPort, exception);
        }
    }
}
