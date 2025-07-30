package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteArrayTag extends NbtTag {

    public ByteArrayTag(String name) {
        super(name, new byte[] {});
        setID(TagDef.ByteArray);
    }

    public ByteArrayTag(String name, byte[] value) {
        super(name, value);
        setPayload();
        setID(TagDef.ByteArray);
    }

    public ByteArrayTag(byte[] value) {
        super(null, value);
        setPayload();
        setID(TagDef.ByteArray);
    }

    public void setValue(byte[] value) {
        super.setValue(value);
        setPayload();
    }

    @Override
    public TagDef getID() {
        return TagDef.ByteArray;
    }

    public int size() {
        return getByteArrayValue().length;
    }

    private void setPayload() {
        byte[] data = getByteArrayValue();
        super.setPayload((byte)(data.length + 4));
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putInt(size());
            for (byte i : (byte[])getValue()) {
                buffer.put(i);
            }

            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
