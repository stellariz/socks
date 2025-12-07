package ru.stellariz.socks.socks5.context;

import ru.stellariz.socks.common.utils.NegotiationVersion;
import ru.stellariz.socks.socks5.exception.UsernamePasswordAuthenticationException;

/**
 * Контекст при username-password аутентификации пользователя
 *
 * @param negotiationVersion версия протокола (НЕ SOCKS)
 * @param username имя пользователя
 * @param password пароль
 * @param exception ошибка при аутентификации пользователя
 */
public record UsernamePasswordAuthenticationContext(
        NegotiationVersion negotiationVersion,
        String username,
        String password,
        UsernamePasswordAuthenticationException exception
) {

    public static UsernamePasswordAuthenticationContextBuilder builder() {
        return new UsernamePasswordAuthenticationContextBuilder();
    }

    public static class UsernamePasswordAuthenticationContextBuilder {
        private NegotiationVersion negotiationVersion;
        private String username;
        private String password;
        private UsernamePasswordAuthenticationException exception;


        public UsernamePasswordAuthenticationException getException() {
            return exception;
        }

        public UsernamePasswordAuthenticationContextBuilder withNegotiationVersion(NegotiationVersion negotiationVersion) {
            this.negotiationVersion = negotiationVersion;
            return this;
        }

        public UsernamePasswordAuthenticationContextBuilder withException(UsernamePasswordAuthenticationException exception) {
            this.exception = exception;
            return this;
        }

        public UsernamePasswordAuthenticationContextBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UsernamePasswordAuthenticationContextBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UsernamePasswordAuthenticationContext build() {
            return new UsernamePasswordAuthenticationContext(negotiationVersion, username, password, exception);
        }
    }
}
