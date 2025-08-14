package com.copperpenguin96.smartnbt.tags;

/**
 * Hosts definitions for the numerous tags defined by NBT.
 * Makes saving/loading/parsing easier.
 */
public enum TagDef {
    /**
     * Tells NBT the end of a file or compound has been reached.
     */
    End((byte) 0),
    Byte((byte) 1),
    Short((byte) 2),
    Int((byte) 3),
    Long((byte) 4),
    Float((byte) 5),
    Double((byte) 6),
    ByteArray((byte) 7),
    String((byte) 8),

    /**
     * A list of tags that have no names. Is not the same as a compound
     */
    List((byte) 9),

    /**
     * A list of tags that have names. Is not the same as a list
     */
    Compound((byte) 10),
    IntArray((byte) 11),
    LongArray((byte) 12),
    Boolean((byte) 1);

    private byte _def;
    private TagDef(byte value) {
        _def = value;
    }

    public TagDef get() {
        return switch (_def) {
            case 0 -> TagDef.End;
            case 1 -> TagDef.Byte;
            case 2 -> TagDef.Short;
            case 3 -> TagDef.Int;
            case 4 -> TagDef.Long;
            case 5 -> TagDef.Float;
            case 6 -> TagDef.Double;
            case 7 -> TagDef.ByteArray;
            case 8 -> TagDef.String;
            case 9 -> TagDef.List;
            case 10 -> TagDef.Compound;
            case 11 -> TagDef.IntArray;
            case 12 -> TagDef.LongArray;
            default -> throw new IllegalArgumentException();
        };
    }

    public byte getByte() {
        return _def;
    }

    public static TagDef getDef(byte def) {
        return switch (def) {
            case 0 -> TagDef.End;
            case 1 -> TagDef.Byte;
            case 2 -> TagDef.Short;
            case 3 -> TagDef.Int;
            case 4 -> TagDef.Long;
            case 5 -> TagDef.Float;
            case 6 -> TagDef.Double;
            case 7 -> TagDef.ByteArray;
            case 8 -> TagDef.String;
            case 9 -> TagDef.List;
            case 10 -> TagDef.Compound;
            case 11 -> TagDef.IntArray;
            case 12 -> TagDef.LongArray;
            default -> throw new IllegalArgumentException();
        };
    }
}
