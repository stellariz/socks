package ru.stellariz.socks.utils;

public class ByteConversionUtils {
    public static int convertToPortNumber(byte b1, byte b2) {
        return (unsignedInt(b1) << 8) | unsignedInt(b2);
    }

    public static byte[] convertPortToBytes(int port) {
        return new byte[]{(byte)(port >> 8), (byte)(port & 0xff)};
    }
    public static int unsignedInt(byte b) {
        return b & 0xFF;
    }
}
