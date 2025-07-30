package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class IntTag extends NbtTag {

    public IntTag(String name) {
        super(name);
        setPayload((byte)4);
        setID(TagDef.Int);
    }

    public IntTag(String name, int value) {
        super(name, value);
        setPayload((byte)4);
        setID(TagDef.Int);
    }

    public IntTag(int value) {
        super(null, value);
        setPayload((byte)4);
        setID(TagDef.Int);
    }

    public void setValue(int value) {
        super.setValue(value);
        setPayload((byte)4);
    }

    @Override
    public TagDef getID() {
        return TagDef.Int;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putInt(getIntValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
