package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class StringTag extends NbtTag {

    public StringTag(String name) {
        super(name);
        setPayload();
        setID(TagDef.String);
    }

    public StringTag(String name, String value) {
        super(name, value);
        setPayload();
        setID(TagDef.String);
    }

    public StringTag() {
        super("");
        setPayload();
        setID(TagDef.String);
    }

    public void setValue(String value) {
        super.setValue(value);
        setPayload();
        setID(TagDef.String);
    }

    @Override
    public TagDef getID() {
        return TagDef.String;
    }

    public int length() {
        return getStringValue().length();
    }

    private void setPayload() {
        super.setPayload((byte)(length() + 2));
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            ByteBuffer buffer = ByteBuffer.allocate(getPayload());
            buffer.putShort((short)2);
            buffer.put(getStringValue().getBytes(StandardCharsets.UTF_8));
            stream.write(buffer.array());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "\"" + getStringValue() + "\"";
    }
}
