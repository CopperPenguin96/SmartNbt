package com.copperpenguin96.smartnbt.tags;

import com.copperpenguin96.smartnbt.NbtFormatException;
import com.copperpenguin96.smartnbt.serialization.SerializerOptions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.util.*;

public class ListTag extends NbtTag {

    private TagDef _type;

    public TagDef getType() {
        return _type;
    }

    public ListTag(String name, TagDef type) {
        super(name);
        setID(TagDef.List);
        _type = type;
    }

    private <T> TagDef getType(T value) throws NbtFormatException {
        NbtTag tag = (NbtTag)value;
        return tag.getID();
    }

    private <T> T[] getTArray(List<T> tA) {
        return (T[])tA.toArray();
    }
    
    public <T> ListTag(String name, ArrayList<T> value) throws NbtFormatException {
        super(name);
        setValue(getTArray(value));
        setPayload();
        setID(TagDef.List);
        _type = getType(value.get(0));
    }

    public <T> ListTag(ArrayList<T> value) throws NbtFormatException {
        this("", value);
    }

    public <T> ListTag(String name, List<T> value) throws NbtFormatException {
        super(name);
        setValue(getTArray(value));
        setPayload();
        setID(TagDef.List);
        _type = getType(value.get(0));
    }

    public <T> ListTag(String name, T[] value) throws NbtFormatException {
        super(name, value);
        setPayload();
        setID(TagDef.List);
        _type = getType(value[0]);
    }

    public <T> ListTag(T[] value) throws NbtFormatException {
        super(null, value);
        setPayload();
        setID(TagDef.List);
        _type = getType(value[0]);
    }

    @Override
    public TagDef getID() {
        return TagDef.List;
    }

    public int size() {
        return getItems().length;
    }

    public void setPayload() {
        byte payload = (byte)getItems().length;

        for (NbtTag tag : getItems()) {
            payload++;
            payload += tag.getPayload();
        }

        super.setPayload(payload);
    }

    public void add(NbtTag tag) {
        List<NbtTag> items = Arrays.stream(getItems()).toList();
        items.add(tag);
        setValue(items);
    }

    public <T> void add(String name, T value) throws InvalidObjectException {
        NbtTag tag = NbtTag.create(name, value);
        add(tag);
    }

    public void clear() {
        setValue(new NbtTag[] {});
        setPayload();
    }

    public boolean contains(NbtTag item) {
        return Arrays.stream(getItems()).toList().contains(item);
    }

    public boolean remove(NbtTag tag) {
        List<NbtTag> li = Arrays.stream(getItems()).toList();
        li.remove(tag);
        setValue(li);
        return true;
    }

    public <T> void setValue(ArrayList<T> li) {
        setValue(getTArray(li));
        setPayload();
    }

    public <T> void setValue(List<T> li) {
        setValue(getTArray(li));
        setPayload();
    }

    public <T> void setValue(T[] arr) {
        super.setValue(arr);
        setPayload();
    }

    public NbtTag get(int index) {
        return getItems()[index];
    }

    @Override
    public void writeToStream(OutputStream stream) {
        super.writeToStream(stream);
        try {
            NbtTag[] items = getItems();
            stream.write(items[0].getID().getByte());
            stream.write(items.length);

            for (NbtTag tag : getItems()) {
                writeTag(stream, tag);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        StringBuilder start = new StringBuilder("[");

        if (size() == 0) return "[]";

        for (int x = 0; x < getItems().length; x++) {
            start.append(getItems()[x].toString());
            if (x < getItems().length - 1) start.append(", ");
        }

        start.append("]");
        return start.toString();
    }

    public String toString(SerializerOptions options) {
        StringBuilder start = new StringBuilder("[");

        if (size() == 0) return "[]";

        for (int x = 0; x < getItems().length; x++) {
            NbtTag tag = getItems()[x];

            // If tag is float or int, instruct to use options as necessary.
            switch (_type) {
                case Float:
                    FloatTag floatTag = (FloatTag)tag;
                    start.append(floatTag.toString(options));
                    break;
                case Int:
                    IntTag intTag = (IntTag)tag;
                    start.append(intTag.toString(options));
                    break;
                default:
                    start.append(tag);
            }

            if (x < getItems().length - 1) start.append(", ");
        }

        start.append("]");
        return start.toString();
    }
}
