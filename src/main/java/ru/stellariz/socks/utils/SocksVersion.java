package ru.stellariz.socks.utils;

public enum SocksVersion {
    SOCKS_4((byte)0x04),
    SOCKS_5((byte)0x05);

    private final byte protocolVersion;

    SocksVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }
}
