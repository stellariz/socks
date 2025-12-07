package ru.stellariz.socks.common.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException() {
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
