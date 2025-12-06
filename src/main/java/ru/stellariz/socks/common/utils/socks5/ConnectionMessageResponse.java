package ru.stellariz.socks.common.utils.socks5;

//  X'00' succeeded
//  X'01' general SOCKS server failure
//  X'02' connection not allowed by ruleset
//  X'03' Network unreachable
//  X'04' Host unreachable
//  X'05' Connection refused
//  X'06' TTL expired
//  X'07' Command not supported
//  X'08' Address type not supported
//  X'09' to X'FF' unassigned
public enum ConnectionMessageResponse {
    SUCCEED((byte)0),
    REQUEST_REJECTED((byte)0x01)
    ;
    private final byte value;

    ConnectionMessageResponse(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
