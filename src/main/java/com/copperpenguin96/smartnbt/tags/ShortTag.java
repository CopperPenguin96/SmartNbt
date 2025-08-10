package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ShortTag extends NbtTag {

    public ShortTag(String name) {
        super(name);
        setPayload((byte)2);
        setID(TagDef.Short);
    }

    public ShortTag(String name, short value) {
        super(name, value);
        setPayload((byte)2);
        setID(TagDef.Short);
    }

    public ShortTag(short value) {
        super(null, value);
        setPayload((byte)2);
        setID(TagDef.Short);
    }

    public void setValue(short value) {
        super.setValue(value);
        setPayload((byte)2);
    }

    @Override
    public TagDef getID() {
        return TagDef.Short;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putShort(getShortValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return getShortValue() + "s";
    }
}
