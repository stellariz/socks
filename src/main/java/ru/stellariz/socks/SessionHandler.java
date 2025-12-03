package ru.stellariz.socks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class SessionHandler {
    private static final int BUFFER_SIZE = 1024 * 1024;

    private final Thread clentThread;
    private final Thread appThread;
    private final Semaphore semaphore = new Semaphore(-1);
    private volatile boolean isRunning = true;

    public SessionHandler(Socket clientSocket, Socket applicationSocket) throws IOException, InterruptedException {
        try (var isOrigin = clientSocket.getInputStream();
             var osOrigin = clientSocket.getOutputStream();
             var isDest = applicationSocket.getInputStream();
             var osDest = applicationSocket.getOutputStream()) {
            clentThread = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isOrigin, osDest));
            appThread = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isDest, osOrigin));
            // Засыпаем на семафоре и ждем когда два потока закончат свою работу
            waitForEndSession();
        }
    }

    public void waitForEndSession() {
        try {
            semaphore.acquire();
            clentThread.join();
            appThread.join();
        } catch (InterruptedException ex) {
            System.err.printf("Thread [%s]: Error occurred in SessionHandler: %s\n",
                    Thread.currentThread().getName(), ex.getMessage());
        }
    }

    private void sendMessageFromSocketToSocket(InputStream is, OutputStream os) {
        byte[] buffer = new byte[BUFFER_SIZE];
        while (isRunning) {
            try {
                int bytesRead = is.read(buffer);
                if (bytesRead > 0) {
                    os.write(buffer, 0, bytesRead);
                }
                if (bytesRead == -1) {
                    isRunning = false;
                    break;
                }
            } catch (IOException ex) {
                System.err.printf("Error during resending message: %s\n", ex.getMessage());
                isRunning = false;
                break;
            }
        }
        semaphore.release();
    }
}
