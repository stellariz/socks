package ru.stellariz.socks.common.utils;

public enum NegotiationVersion {
    V1((byte)0x01);

    private final byte negotiationVersion;

    NegotiationVersion(byte negotiationVersion) {
        this.negotiationVersion = negotiationVersion;
    }

    public static NegotiationVersion fromByte(byte ver) {
        for (var versions : values()) {
            if (versions.negotiationVersion == ver) {
                return versions;
            }
        }
        return null;
    }

    public byte getNegotiationVersion() {
        return negotiationVersion;
    }
}
