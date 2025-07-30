package com.copperpenguin96.smartnbt.tags;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.util.*;

public class ListTag<T> extends NbtTag {

    public ListTag(String name) {
        super(name);
        setID(TagDef.List);
    }

    public ListTag(ListTag<T> tag) {
        this(tag.Name);
        setValue(tag.getValue());
        setPayload();
        setID(TagDef.List);
    }

    public ListTag(BasicListTag basicList) {
        this((ListTag<T>) basicList);
    }

    private T[] getTArray(List<T> tA) {
        return (T[])tA.toArray();
    }
    
    public ListTag(String name, ArrayList<T> value) {
        super(name);
        setValue(getTArray(value));
        setPayload();
        setID(TagDef.List);
    }

    public ListTag(String name, List<T> value) {
        super(name);
        setValue(getTArray(value));
        setPayload();
        setID(TagDef.List);
    }

    public ListTag(String name, T[] value) {
        super(name, value);
        setPayload();
        setID(TagDef.List);
    }

    public ListTag(T[] value) {
        super(null, value);
        setPayload();
        setID(TagDef.List);
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

    public void setValue(ArrayList<T> li) {
        setValue(getTArray(li));
        setPayload();
    }

    public void setValue(List<T> li) {
        setValue(getTArray(li));
        setPayload();
    }

    public void setValue(T[] arr) {
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
}
