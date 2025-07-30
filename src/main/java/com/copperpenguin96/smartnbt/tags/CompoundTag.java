package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.util.*;

public class CompoundTag extends NbtTag {

    public CompoundTag(String name) {
        super(name);
        setID(TagDef.Compound);
    }

    public CompoundTag(CompoundTag tag) {
        this(tag.Name);
        setValue(tag.getValue());
        setPayload();
        setID(TagDef.Compound);
    }

    public CompoundTag(String name, ArrayList<NbtTag> value) {
        super(name);
        super.setValue(toArray(value));
        setPayload();
        setID(TagDef.Compound);
    }

    public CompoundTag(String name, List<NbtTag> value) {
        super(name);
        super.setValue(toArray(value));
        setPayload();
        setID(TagDef.Compound);
    }

    public CompoundTag(String name, NbtTag[] value) {
        super(name, value);
        setPayload();
        setID(TagDef.Compound);
    }

    public CompoundTag(NbtTag[] value) {
        super(null, value);
        setPayload();
        setID(TagDef.Compound);
    }

    @Override
    public TagDef getID() {
        return TagDef.Compound;
    }

    public int size() {
        return getItems().length;
    }

    public void setPayload() {
        byte payload = (byte)getItems().length;

        for (NbtTag tag : getItems()) {
            payload += tag.getPayload();
        }

        super.setPayload(payload);
    }

    public <T> void add(T value) throws InvalidObjectException {
        NbtTag tag = null;
        if (value instanceof boolean) {
            tag = new BoolTag(Name, (boolean)value);
        } else if (value instanceof byte[]) {
            tag = new ByteArrayTag(Name, (byte[])value);
        } else if (value instanceof byte) {
            tag = new ByteTag(Name, (byte)value);
        } else if (value instanceof CompoundTag) {
            tag = new CompoundTag((CompoundTag)value);
        } else if (value instanceof double) {
            // todo setup rest of data types
        } else {
            throw new InvalidObjectException("value is not applicable type");
        }

        add(tag);
    }

    public void add(NbtTag tag) {
        List<NbtTag> items = Arrays.stream(getItems()).toList();
        items.add(tag);
        setValue(items);
    }

    public void clear() {
        setValue(new NbtTag[] {});
        setPayload();
    }

    public boolean contains(NbtTag item) {
        return Arrays.stream(getItems()).toList().contains(item);
    }

    public boolean containsKey(String tagName) {
        for (NbtTag tag : getItems()) {
            if (tag.Name.equals(tagName)) return true;
        }

        return false;
    }

    public NbtTag getTag(String name) {
        for (NbtTag tag : getItems()) {
            if (tag.Name.equals(name)) {
                return tag;
            }
        }

        return null;
    }

    public boolean remove(NbtTag tag) {
        List<NbtTag> li = Arrays.stream(getItems()).toList();
        li.remove(tag);
        setValue(li);
        return true;
    }

    public boolean remove(String tagName) {
        for (NbtTag tag : getItems()) {
            if (tag.Name.equals(tagName)) {
                remove(tag);
                setPayload();
                return true;
            }
        }

        return false;
    }

    public void setValue(ArrayList<NbtTag> li) {
        setValue(toArray(li));
        setPayload();
    }

    public void setValue(List<NbtTag> li) {
        setValue(toArray(li));
        setPayload();
    }

    public void setValue(NbtTag[] arr) {
        super.setValue(arr);
        setPayload();
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            for (NbtTag tag : getItems()) {
                writeTag(stream, tag);
            }

            new EndTag().writeToStream(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
