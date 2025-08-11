package com.copperpenguin96.smartnbt.tags;

import com.copperpenguin96.smartnbt.serialization.SerializerOptions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.util.*;

public class CompoundTag extends NbtTag {

    public CompoundTag() {
        super("");
        setID(TagDef.Compound);
        setValue(new NbtTag[]{});
    }

    public CompoundTag(String name) {
        super(name);
        setID(TagDef.Compound);
        setValue(new NbtTag[]{});
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

    public <T> void add(String name, T value) throws InvalidObjectException {
        NbtTag tag = NbtTag.create(name, value);
        add(tag);
    }

    public void add(NbtTag tag) {
        if (getItems() == null) {
            setValue(new ArrayList<>());
        }

        ArrayList<NbtTag> items = getArrayList();
        items.add(tag);
        setValue(items);
    }

    private ArrayList<NbtTag> getArrayList() {
        return new ArrayList<>(Arrays.asList(getItems()));
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

    @Override
    public String toString() {
        StringBuilder compBuilder = new StringBuilder("{");
        if (size() == 0) return "{}";

        for (int x = 0; x < size(); x++) {
            String name = getItems()[x].Name;

            if (!name.matches("^[0-9A-Za-z_\\-.+]+$")) {
                name = "\"" + name + "\"";
            }

            name += ":";

            compBuilder.append(name);
            compBuilder.append(getItems()[x].toString());

            if (x < getItems().length - 1) compBuilder.append(",");
        }

        compBuilder.append("}");
        return compBuilder.toString();
    }

    public String toString(SerializerOptions options) {
        StringBuilder compBuilder = new StringBuilder("{");
        if (size() == 0) return "{}";

        for (int x = 0; x < size(); x++) {
            NbtTag tag = getItems()[x];
            String name = tag.Name;

            if (!name.matches("^[0-9A-Za-z_\\-.+]+$")) {
                name = "\"" + name + "\"";
            }

            name += ":";

            compBuilder.append(name);

            // If tag is float or int, instruct to use options as necessary.
            switch (tag.getID()) {
                case Float:
                    FloatTag floatTag = (FloatTag)tag;
                    compBuilder.append(floatTag.toString(options));
                    break;
                case Int:
                    IntTag intTag = (IntTag)tag;
                    compBuilder.append(intTag.toString(options));
                    break;
                default:
                    compBuilder.append(tag);
            }

            if (x < getItems().length - 1) compBuilder.append(",");
        }

        compBuilder.append("}");
        return compBuilder.toString();
    }
}
