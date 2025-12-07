package ru.stellariz.socks.socks5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ru.stellariz.socks.socks5.connection.handlers.ClientConnectionHandler;

/**
 * SOCKS5 server
 */
public class Socks5Server implements Runnable {
    // Порт сервера для подключения
    private int port;

    private Socks5Server() {
    }

    public void start() {
        Thread.ofPlatform().start(this);
    }

    @Override
    public void run() {
        System.out.println("Server is running!");
        try (var s = new ServerSocket(port)) {
            while (true) {
                Socket acceptedClient = s.accept();
                System.out.printf("Thread [%s]: Client connection [%s] was accepted\n",
                        Thread.currentThread().getName(),
                        acceptedClient.getLocalAddress().toString());
                Thread.ofPlatform().start(() -> new ClientConnectionHandler(acceptedClient));
            }
        } catch (IOException e) {
            System.err.printf("Thread [%s]: Error during open socket: [%s]\n",
                    Thread.currentThread().getName(),
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static SocksServerBuilder builder() {
        return new SocksServerBuilder();
    }

    public static class SocksServerBuilder {
        private final Socks5Server socksServer;

        public SocksServerBuilder() {
            this.socksServer = new Socks5Server();
        }

        public SocksServerBuilder withPort(int port) {
            socksServer.setPort(port);
            return this;
        }

        public Socks5Server build() {
            return socksServer;
        }
    }
}
