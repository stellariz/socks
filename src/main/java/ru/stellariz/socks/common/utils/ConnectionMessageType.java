package ru.stellariz.socks.common.utils;

public enum ConnectionMessageType {
    CONNECT((byte)0x01),
    BIND((byte)0x02),
    UDP_ASSOCIATE((byte)0x03);

    private final byte operationByte;

    ConnectionMessageType(byte operationByte) {
        this.operationByte = operationByte;
    }

    public static ConnectionMessageType fromByte(byte cd) {
        for (var connection : values()) {
            if (connection.operationByte == cd) {
                return connection;
            }
        }
        return null;
    }
}
