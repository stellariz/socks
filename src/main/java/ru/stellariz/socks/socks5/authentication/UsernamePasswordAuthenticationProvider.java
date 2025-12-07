package ru.stellariz.socks.socks5.authentication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import ru.stellariz.socks.socks5.authentication.username_password.FailedUserAuthenticationHandler;
import ru.stellariz.socks.socks5.authentication.username_password.SucceedUserAuthenticationHandler;
import ru.stellariz.socks.socks5.authentication.username_password.UsernamePasswordAuthenticationChainProcessor;
import ru.stellariz.socks.socks5.connection.authentication.AuthenticationResult;
import ru.stellariz.socks.socks5.context.UsernamePasswordAuthenticationContext;

public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private static final UsernamePasswordAuthenticationChainProcessor chainProcessor =
            new UsernamePasswordAuthenticationChainProcessor();

    private static final Map<AuthenticationResult, UserAuthenticationHandler> authHandlersMap =
            Map.of(
                    AuthenticationResult.FAILED, new FailedUserAuthenticationHandler(),
                    AuthenticationResult.SUCCEED, new SucceedUserAuthenticationHandler()
            );

    // Может быть вынесено в перменные окружения
    private static final Set<User> SOCKS_USERS = Set.of(
            new User("Ruslan", "Test")
    );

    @Override
    public AuthenticationResult authenticate(InputStream is, OutputStream os) throws IOException {
        UsernamePasswordAuthenticationContext authContext = chainProcessor.buildContextFromClientRequest(is);
        if (authContext.exception() != null) {
            authHandlersMap.get(AuthenticationResult.FAILED).notifyUser(os, authContext);
            return AuthenticationResult.FAILED;
        }
        var authenticatedUser = new User(authContext.username(), authContext.password());
        AuthenticationResult authenticationResult = SOCKS_USERS.contains(authenticatedUser) ?
                AuthenticationResult.SUCCEED :
                AuthenticationResult.FAILED;
        UserAuthenticationHandler userAuthenticationHandler = authHandlersMap.get(authenticationResult);
        userAuthenticationHandler.notifyUser(os, authContext);
        return authenticationResult;
    }

    private record User(
            String username,
            String password
    ) {
    }
}
