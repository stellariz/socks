package ru.stellariz.socks.socks5.context;

import java.util.List;
import ru.stellariz.socks.common.utils.SocksVersion;
import ru.stellariz.socks.socks5.authentication.AuthenticationMethod;
import ru.stellariz.socks.socks5.exception.AuthenticationException;

public record AuthenticationContext(
        SocksVersion protocolVersion,
        int methodsNumber,
        List<AuthenticationMethod> clientAuthentication,
        AuthenticationException exception
) {

    public static AuthenticationContextBuilder builder() {
        return new AuthenticationContextBuilder();
    }

    public static class AuthenticationContextBuilder {
        private SocksVersion protocolVersion;
        private int methodsNumber;
        private List<AuthenticationMethod> clientAuthentication;
        private AuthenticationException exception;


        public AuthenticationContextBuilder withProtocolVersion(SocksVersion protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public AuthenticationContextBuilder withMethodsNumber(int methodsNumber) {
            this.methodsNumber = methodsNumber;
            return this;
        }

        public AuthenticationContextBuilder withClientAuthentication(List<AuthenticationMethod> clientAuthentication) {
            this.clientAuthentication = clientAuthentication;
            return this;
        }

        public AuthenticationContextBuilder withException(AuthenticationException authenticationException) {
            this.exception = exception;
            return this;
        }

        public AuthenticationContext build() {
            return new AuthenticationContext(protocolVersion, methodsNumber, clientAuthentication, exception);
        }

        public AuthenticationException getException() {
            return exception;
        }

        public List<AuthenticationMethod> getClientAuthentication() {
            return clientAuthentication;
        }

        public int getMethodsNumber() {
            return methodsNumber;
        }
    }
}
