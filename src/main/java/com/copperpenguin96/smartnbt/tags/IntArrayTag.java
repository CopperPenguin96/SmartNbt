package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class IntArrayTag extends NbtTag {

    public IntArrayTag(String name) {
        super(name, new int[]{});
        setID(TagDef.IntArray);
    }

    public IntArrayTag(String name, int[] value) {
        super(name, value);
        setPayload();
        setID(TagDef.IntArray);
    }

    public IntArrayTag(int[] value) {
        super(null, value);
        setPayload();
        setID(TagDef.IntArray);
    }

    public void add(int i) {
        ArrayList<Integer> t = new ArrayList<>();

        for (int v : getIntArrayValue()) {
            t.add(v);
        }

        t.add(i);
        setValue(t.stream().toArray());
    }

    public void setValue(int[] value) {
        super.setValue(value);
        setPayload();
    }

    @Override
    public TagDef getID() {
        return TagDef.IntArray;
    }

    public int size() {
        return getIntArrayValue().length;
    }

    private void setPayload() {
        int[] data = getIntArrayValue();
        super.setPayload((byte)(data.length * 4 + 4));
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putInt(size());
            for (int i : (int[])getValue()) {
                buffer.putInt(i);
            }

            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
