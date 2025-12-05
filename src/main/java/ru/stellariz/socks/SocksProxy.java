package ru.stellariz.socks;

import ru.stellariz.socks.socks4.Socks4Server;

public class SocksProxy {
    private static final int DEFAULT_SOCKS_PORT = 1080;

    public static void main(String[] args) {
        int socksServerPort = resolvePort(args);

        Socks4Server socksServer = Socks4Server.builder()
                .withPort(socksServerPort)
                .build();

        socksServer.start();
    }

    private static int resolvePort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_SOCKS_PORT;
        }
        return Integer.parseInt(args[0]);
    }
}
