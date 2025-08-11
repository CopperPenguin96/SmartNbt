package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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

    public ByteArrayTag(ArrayList<Byte> value) {
        this ("", value);
    }

    public ByteArrayTag(String name, ArrayList<Byte> value) {
        super(name);
        setID(TagDef.ByteArray);

        byte[] bts = new byte[value.size()];

        for (int x = 0; x < value.size(); x++) {
            bts[x] = value.get(x);
        }

        setValue(bts);
        setPayload();
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

    public void add(byte bt) {
        ArrayList<Byte> bts = getArrayList();
        bts.add(bt);
        setValue(getByteArray(bts.toArray()));
    }

    public void delete(int index) {
        ArrayList<Byte> bts = getArrayList();
        bts.remove(index);
        setValue(getByteArray(bts.toArray()));
    }

    private ArrayList<Byte> getArrayList() {
        ArrayList<Byte> bts = new ArrayList<>();
        for (byte b : getByteArrayValue()) {
            bts.add(b);
        }
        return bts;
    }

    private byte[] getByteArray(Object[] o) {
        byte[] bts = new byte[o.length];
        for (int x = 0; x < o.length; x++) {
            bts[x] = (byte)o[x];
        }

        return bts;
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

    @Override
    public String toString() {
        StringBuilder bui = new StringBuilder("[B;");

        for (int x = 0; x < size(); x++) {
            bui.append(getByteArrayValue()[x]).append("b");
            if (x < size() - 1) bui.append(",");
        }

        bui.append("]");
        return bui.toString();
    }
}
