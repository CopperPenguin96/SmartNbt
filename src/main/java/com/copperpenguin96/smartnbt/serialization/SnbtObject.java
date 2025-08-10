package com.copperpenguin96.smartnbt.serialization;

import com.copperpenguin96.smartnbt.tags.CompoundTag;
import com.copperpenguin96.smartnbt.tags.NbtTag;

public class SnbtObject {
    private CompoundTag _root;

    public SnbtObject() {
        this(new CompoundTag(""));
    }

    public SnbtObject(CompoundTag root) {
        _root = root;
    }

    /**
     * Adds a tag to the root compound.
     */
    public void add(NbtTag tag) {
        _root.add(tag);
    }

    /**
     * Removes a tag from the root compound
     * @param tag
     */
    public void delete(NbtTag tag) {
        _root.remove(tag);
    }

    public void setSerializerOptions(SerializerOptions... options) {
        for (SerializerOptions opt : options) {
            if (opt instanceof FloatSerializerOptions) {

            }
        }
    }
}
