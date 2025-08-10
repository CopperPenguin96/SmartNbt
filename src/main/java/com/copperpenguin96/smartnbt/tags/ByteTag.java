package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;

public class ByteTag extends NbtTag {

    public ByteTag(String name) {
        super(name);
        setPayload((byte)1);
        setID(TagDef.Byte);
    }

    public ByteTag(String name, byte value) {
        super(name, value);
        setPayload((byte)1);
        setID(TagDef.Byte);
    }

    public ByteTag(byte value) {
        super(null, value);
        setPayload((byte)1);
        setID(TagDef.Byte);
    }

    public void setValue(byte value) {
        super.setValue(value);
    }

    @Override
    public TagDef getID() {
        return TagDef.Byte;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            stream.write(getByteValue());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getByteValue() + "b";
    }
}
