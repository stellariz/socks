package ru.stellariz.socks.socks5.exception;

public class UsernamePasswordAuthenticationException extends RuntimeException {
    public UsernamePasswordAuthenticationException(Throwable cause) {
        super(cause);
    }

    public UsernamePasswordAuthenticationException(String message) {
        super(message);
    }
}
