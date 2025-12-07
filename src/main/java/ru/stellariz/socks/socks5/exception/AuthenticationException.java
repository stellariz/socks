package ru.stellariz.socks.socks5.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
