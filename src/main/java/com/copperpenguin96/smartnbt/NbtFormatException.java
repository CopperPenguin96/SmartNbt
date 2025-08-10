package com.copperpenguin96.smartnbt;

/**
 * Throws when there's formatting exceptions with NBT. (like inproper names, invalid SNBT parsing, etc.)
 */
public class NbtFormatException extends Exception {
    public NbtFormatException() {
        super();
    }

    public NbtFormatException(String message) {
        super(message);
    }

    public NbtFormatException(String message, Throwable inner) {
        super(message, inner);
    }

    public NbtFormatException(Throwable inner) {
        super(inner);
    }
}
