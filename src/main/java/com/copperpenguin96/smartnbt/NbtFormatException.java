package com.copperpenguin96.smartnbt;

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
