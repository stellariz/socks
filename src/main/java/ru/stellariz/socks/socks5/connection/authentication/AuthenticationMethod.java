package ru.stellariz.socks.socks5.connection.authentication;

import java.util.List;

public enum AuthenticationMethod {
    NO_AUTH((byte)0),
    GSSAPI((byte)0x01),
    USERNAME_PASSWORD((byte)0x02),
    NO_AVAILABLE((byte)0xFF);

    private final byte value;

    AuthenticationMethod(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static AuthenticationMethod fromByte(byte auth) {
        for (var connection : values()) {
            if (connection.value == auth) {
                return connection;
            }
        }
        return null;
    }

    public static AuthenticationMethod findFirstAvailableAuthenticationMethod(
            List<AuthenticationMethod> authenticationMethodList) {
        for (var authMethod : AuthenticationMethod.values()) {
            if (authenticationMethodList.contains(authMethod)) {
                return authMethod;
            }
        }
        return NO_AVAILABLE;
    }
}
