package ru.stellariz.socks.socks5.context;

public enum AddressType {
    IP_V4((byte)0x01, 4),
    DOMAIN((byte)0x03, -1), // length is defined during request
    IP_V6((byte)0x04, 16);

    private final byte value;
    private final int bytesLength;

    AddressType(byte value, int bytesLength) {
        this.value = value;
        this.bytesLength = bytesLength;
    }

    public static AddressType fromByte(byte atyp) {
        for (var connection : values()) {
            if (connection.value == atyp) {
                return connection;
            }
        }
        return null;
    }

    public byte getValue() {
        return value;
    }

    public int getBytesLength() {
        return bytesLength;
    }
}
