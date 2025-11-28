package ru.stellariz.socks.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class ConnectionHandler {

    private static final int BUFFER_SIZE = 1024 * 1024;

    private final Socket client;

    private ConnectionState connectionState;
    private String clientId;
    private volatile boolean closed;

    public ConnectionHandler(Socket client) {
        this.client = client;
        this.connectionState = ConnectionState.NEW;
        try (var isOrigin = client.getInputStream();
             var osOrigin = client.getOutputStream();
             var destSocket = handleHelloMessage(isOrigin, osOrigin)) {
            if (destSocket == null) {
                System.err.printf("Error to open connection to dest from: %s\n", client.getInetAddress().getHostName());
                return;
            }
            // TODO(r.popov): note about missing handling BIND message
            try (var isDest = destSocket.getInputStream();
                 var osDest = destSocket.getOutputStream()) {
                var t1 = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isOrigin, osDest));
                var t2 = Thread.ofVirtual().start(() -> sendMessageFromSocketToSocket(isDest, osOrigin));

                t1.join();
                t2.join();

                client.close();
            }
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void sendMessageFromSocketToSocket(InputStream is, OutputStream os) {
        byte[] buffer = new byte[1024 * 1024 * 5];
        while (!closed) {
            try {
                int bytesRead;
                while ((bytesRead = is.read(buffer)) > 0) {
                    os.write(buffer, 0, bytesRead);
                    os.flush();
                }
                if (bytesRead == -1) {
                    closed = true;
                }
            } catch (IOException ex) {
                closed = true;
            }
        }
    }

    private Socket handleHelloMessage(InputStream is, OutputStream os) throws IOException {
        readSocksVersion(is);
        readCd(is);
        var dstPort = readDstPort(is);
        var dstIp = readDstIp(is);
        var userId = readUserId(is);
        if (userId == null) {
            clientId = UUID.randomUUID().toString();
        } else {
            clientId = new String(userId);
        }
        System.out.printf("%s send hello message\n", clientId);

        BigInteger port = new BigInteger(dstPort);
        InetAddress inetAddress = resolveAddress(dstIp, is);
        Socket destSocket = null;
        try {
            destSocket = new Socket(inetAddress, port.intValue());
            sendMessage(createSucceedConnectionEstablishedMessage(), os);
            this.connectionState = ConnectionState.ESTABLISHED;
            System.out.printf("Connection to %s:%d established\n", inetAddress.getHostName(), port);
        } catch (IOException ex) {
            this.connectionState = ConnectionState.REJECTED;
            sendMessage(createFailedConnectionEstablishedMessage(), os);
            System.err.println("Unable to open connection to destination");
        }
        return destSocket;
    }

    private static InetAddress resolveAddress(byte[] dstIp, InputStream is) throws UnknownHostException {
        // socks 4a 😈
        if (checkIsDnsRequest(dstIp)) {
            return InetAddress.getByName(readDestIpFromDnsQuery(is));
        }
        return InetAddress.getByAddress(dstIp);
    }


    private static boolean checkIsDnsRequest(byte[] dstIp) {
        return dstIp[0] == 0 && dstIp[1] == 0 && dstIp[2] == 0 && dstIp[3] != 0;
    }

    private static byte[] readSocksVersion(InputStream is) {
        byte[] socksVersion = new byte[1];
        try {
            int totalRead = is.read(socksVersion);
            if (totalRead == -1) {
                throw new IOException("No data read for SOCKS version");
            }
            if (socksVersion[0] != BYTES_INFO.SOCKS4_VERSION.value) {
                throw new IllegalArgumentException("Unsupported protocol version");
            }
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
        return socksVersion;
    }

    private static byte[] readCd(InputStream is) {
        byte[] cd = new byte[1];
        try {
            // TODO(r.popov): replace: is.read()
            int totalRead = is.read(cd);
            if (totalRead == -1) {
                throw new IOException("No data read for cd value");
            }
            if (cd[0] != BYTES_INFO.CD_CONNECT_VALUE.value) {
                throw new IllegalArgumentException("Invalid data read for cd value");
            }
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
        return cd;
    }

    private static byte[] readDstPort(InputStream is) {
        byte[] dstPort = new byte[2];
        try {
            int totalRead = is.read(dstPort);
            if (totalRead != 2) {
                throw new IOException("Invalid data read for port destination");
            }
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
        return dstPort;
    }

    private static byte[] readUserId(InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int b;
            while ((b = is.read()) != -1) {
                if (b == 0) {
                    break;
                }
                byteArrayOutputStream.write(b);
            }

            byte[] userId = byteArrayOutputStream.toByteArray();
            return userId.length > 0 ? userId : null;
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static String readDestIpFromDnsQuery(InputStream is) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            int b;
            while ((b = is.read()) != -1) {
                if (b == 0) {
                    break;
                }
                byteArrayOutputStream.write(b);
            }
            return byteArrayOutputStream.toString();
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static byte[] readDstIp(InputStream is) {
        byte[] dstIp = new byte[4];
        try {
            int totalRead = is.read(dstIp);
            if (totalRead != 4) {
                throw new IOException("Invalid data read for destination ip");
            }
        } catch (IOException e) {
            System.err.printf("Error during hello message from client: [%s]\n", e.getMessage());
            throw new RuntimeException(e);
        }
        return dstIp;
    }

    private void sendMessage(byte[] message, OutputStream os) throws IOException {
        os.write(message);
    }

    private byte[] createSucceedConnectionEstablishedMessage() {
        // last 6 bytes are ignored by client
        return new byte[]{0, (byte) 0x5a, 0, 0, 0, 0, 0, 0};
    }

    private byte[] createFailedConnectionEstablishedMessage() {
        return new byte[]{0, (byte) 0x5b, 0, 0};
    }


    enum ConnectionState {
        NEW,
        ESTABLISHED,
        REJECTED,
        CLOSED
    }

    enum BYTES_INFO {
        SOCKS4_VERSION((byte) 0x04),
        CD_CONNECT_VALUE((byte) 0x01),
        CD_BIND_VALUE((byte) 0x02);

        private final byte value;

        BYTES_INFO(byte value) {
            this.value = value;
        }
    }
}
