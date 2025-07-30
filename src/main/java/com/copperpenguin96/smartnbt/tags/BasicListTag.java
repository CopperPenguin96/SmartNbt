package com.copperpenguin96.smartnbt.tags;

import java.util.ArrayList;
import java.util.List;

public class BasicListTag extends ListTag<NbtTag> {

    public BasicListTag(String name) {
        super(name);
    }

    public BasicListTag(ListTag<NbtTag> tag) {
        super(tag);
    }

    public BasicListTag(BasicListTag basicList) {
        super(basicList);
    }

    public BasicListTag(String name, ArrayList<NbtTag> value) {
        super(name, value);
    }

    public BasicListTag(String name, List<NbtTag> value) {
        super(name, value);
    }

    public BasicListTag(String name, NbtTag[] value) {
        super(name, value);
    }

    public BasicListTag(NbtTag[] value) {
        super(value);
    }
}
