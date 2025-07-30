package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A non-official tag.
 * Just a byte tag as a 0 or a 1.
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
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);

        try {
            stream.write((getByteValue()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
