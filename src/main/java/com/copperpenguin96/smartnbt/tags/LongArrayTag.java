package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class LongArrayTag extends NbtTag {

    public LongArrayTag(String name) {
        super(name, new long[]{});
        setID(TagDef.LongArray);
    }

    public LongArrayTag(String name, long[] value) {
        super(name, value);
        setPayload();
        setID(TagDef.LongArray);
    }

    public LongArrayTag(long[] value) {
        super(null, value);
        setPayload();
        setID(TagDef.LongArray);
    }

    public void add(long i) {
        ArrayList<Long> t = new ArrayList<>();

        for (long v : getLongArrayValue()) {
            t.add(v);
        }

        t.add(i);
        setValue(t.stream().toArray());
    }

    public void setValue(long[] value) {
        super.setValue(value);
        setPayload();
    }

    @Override
    public TagDef getID() {
        return TagDef.LongArray;
    }

    public int size() {
        return getLongArrayValue().length;
    }

    private void setPayload() {
        long[] data = getLongArrayValue();
        super.setPayload((byte)(data.length * 8 + 4));
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putInt(size());
            for (long l : (long[])getValue()) {
                buffer.putLong(l);
            }

            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
