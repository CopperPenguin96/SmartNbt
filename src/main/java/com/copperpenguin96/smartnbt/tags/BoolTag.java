package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An unofficial  tag (SNBT recognized)
 * Just a byte represented as a 0 (false) or a 1 (true)
 */
public class BoolTag extends ByteTag {

    public BoolTag(String name) {
        super(name);

        setPayload((byte)1);
        setID(TagDef.Boolean);
    }

    public BoolTag(String name, boolean value) {
        super(name, value ? (byte)1:(byte)0);

        setPayload((byte)1);
        setID(TagDef.Boolean);
    }

    public BoolTag(boolean value) {
        super(null, value ? (byte)1:(byte)0);

        setPayload((byte)1);
        setID(TagDef.Boolean);
    }

    public void setValue(boolean value) {
        if (value) super.setValue((byte)1);
        else super.setValue((byte)0);
    }

    @Override
    public String toString() {
        if (getBoolValue()) return "true";
        else return "false";
    }
}
