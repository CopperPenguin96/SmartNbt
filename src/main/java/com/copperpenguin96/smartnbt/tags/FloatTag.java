package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class FloatTag extends NbtTag {

    public FloatTag(String name) {
        super(name);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public FloatTag(String name, float value) {
        super(name, value);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public FloatTag(float value) {
        super(null, value);
        setPayload((byte)4);
        setID(TagDef.Float);
    }

    public void setValue(float value) {
        super.setValue(value);
        setPayload((byte)4);
    }

    @Override
    public TagDef getID() {
        return TagDef.Float;
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putFloat(getFloatValue());
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
