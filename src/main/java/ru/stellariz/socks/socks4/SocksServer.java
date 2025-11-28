package ru.stellariz.socks.socks4;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import ru.stellariz.socks.handlers.ConnectionHandler;

/**
 * SOCKS4/4a server
 */
public class SocksServer implements Runnable {
    // Порт сервера для подключения
    private int port;

    private SocksServer() {}


    public void start() {
        Thread.ofPlatform().start(this);
    }

    @Override
    public void run() {
        System.out.println("Server is running!");
        try (var s = new ServerSocket(port)) {
            while (true) {
                Socket acceptedClient = s.accept();
                System.out.printf("Client connection [%s] was accepted\n", acceptedClient.getLocalAddress().toString());
                Thread.ofPlatform().start(() -> new ConnectionHandler(acceptedClient));
            }
        } catch (IOException e) {
            // TODO(r.popov): provide logging
            System.err.printf("Error during open socket: [%s]\n", e.getMessage());
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
        private final SocksServer socksServer;

        public SocksServerBuilder() {
            this.socksServer = new SocksServer();
        }

        public SocksServerBuilder withPort(int port) {
            socksServer.setPort(port);
            return this;
        }

        public SocksServer build() {
            return socksServer;
        }
    }
}
