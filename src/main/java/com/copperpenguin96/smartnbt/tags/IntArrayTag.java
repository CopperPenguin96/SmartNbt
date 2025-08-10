package com.copperpenguin96.smartnbt.tags;

import com.copperpenguin96.smartnbt.serialization.SerializerOptions;

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

    public void delete(int index) {
        ArrayList<Integer> bts = getArrayList();
        bts.remove(index);
        setValue(getIntArray(bts.toArray()));
    }

    private ArrayList<Integer> getArrayList() {
        ArrayList<Integer> bts = new ArrayList<>();
        for (int b : getIntArrayValue()) {
            bts.add(b);
        }
        return bts;
    }

    private int[] getIntArray(Object[] o) {
        int[] bts = new int[o.length];
        for (int x = 0; x < o.length; x++) {
            bts[x] = (int)o[x];
        }

        return bts;
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

    @Override
    public String toString() {
        StringBuilder bui = new StringBuilder("[I;");

        for (int x = 0; x < size(); x++) {
            bui.append(getIntArrayValue()[x]).append("i");
            if (x < size() - 1) bui.append(",");
        }

        bui.append("]");
        return bui.toString();
    }
}
