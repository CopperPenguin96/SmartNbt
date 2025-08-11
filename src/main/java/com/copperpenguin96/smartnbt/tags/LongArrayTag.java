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

    public LongArrayTag(ArrayList<Long> value) {
        this("", value);
    }

    public LongArrayTag(String name, ArrayList<Long> value) {
        super(name);
        setID(TagDef.LongArray);

        long[] bts = new long[value.size()];

        for (int x = 0; x < value.size(); x++) {
            bts[x] = value.get(x);
        }

        setValue(bts);
        setPayload();
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

    public void delete(int index) {
        ArrayList<Long> bts = getArrayList();
        bts.remove(index);
        setValue(getLongArray(bts.toArray()));
    }

    private ArrayList<Long> getArrayList() {
        ArrayList<Long> bts = new ArrayList<>();
        for (long b : getLongArrayValue()) {
            bts.add(b);
        }
        return bts;
    }

    private long[] getLongArray(Object[] o) {
        long[] bts = new long[o.length];
        for (int x = 0; x < o.length; x++) {
            bts[x] = (long)o[x];
        }

        return bts;
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

    @Override
    public String toString() {
        StringBuilder bui = new StringBuilder("[L;");

        for (int x = 0; x < size(); x++) {
            bui.append(getLongArrayValue()[x]).append("l");
            if (x < size() - 1) bui.append(",");
        }

        bui.append("]");
        return bui.toString();
    }
}
