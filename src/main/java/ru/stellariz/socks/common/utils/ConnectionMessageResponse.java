package ru.stellariz.socks.common.utils;

//	90: request granted
//	91: request rejected or failed
//	92: request rejected becasue SOCKS server cannot connect to
//    identd on the client
//	93: request rejected because the client program and identd
//    report different user-ids
public enum ConnectionMessageResponse {
    REQUEST_GRANTED((byte)0x5a),
    REQUEST_REJECTED((byte)0x5b)
    ;
    private final byte value;

    ConnectionMessageResponse(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
