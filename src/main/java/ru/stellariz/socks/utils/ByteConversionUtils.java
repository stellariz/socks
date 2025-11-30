package ru.stellariz.socks.utils;

public class ByteConversionUtils {
    public static int convertToPortNumber(byte b1, byte b2) {
        return (unsignedInt(b1) << 8) | unsignedInt(b2);
    }

    public static int unsignedInt(byte b) {
        return b & 0xFF;
    }
}
