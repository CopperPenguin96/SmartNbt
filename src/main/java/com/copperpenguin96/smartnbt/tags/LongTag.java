package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class LongTag extends NbtTag {

    public LongTag(String name) {
        super(name);
        setPayload((byte)8);
        setID(TagDef.Long);
    }

    public LongTag(String name, long value) {
        super(name, value);
        setPayload((byte)8);
        setID(TagDef.Long);
    }

    public LongTag(long value) {
        super(null, value);
        setPayload((byte)8);
        setID(TagDef.Long);
    }

    public void setValue(long value) {
        super.setValue(value);
        setPayload((byte)8);
    }

    @Override
    public TagDef getID() {
        return TagDef.Long;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putLong(getLongValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
