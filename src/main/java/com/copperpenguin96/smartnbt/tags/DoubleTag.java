package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class DoubleTag extends NbtTag {

    public DoubleTag(String name) {
        super(name);
        setPayload((byte)8);
        setID(TagDef.Double);
    }

    public DoubleTag(String name, double value) {
        super(name, value);
        setPayload((byte)8);
        setID(TagDef.Double);
    }

    public DoubleTag(double value) {
        super(null, value);
        setPayload((byte)8);
        setID(TagDef.Double);
    }

    public void setValue(double value) {
        super.setValue(value);
        setPayload((byte)8);
    }

    @Override
    public TagDef getID() {
        return TagDef.Double;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putDouble(getDoubleValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
