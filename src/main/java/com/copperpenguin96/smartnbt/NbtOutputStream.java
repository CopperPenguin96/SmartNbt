package com.copperpenguin96.smartnbt;

import com.copperpenguin96.smartnbt.tags.NbtTag;

import java.io.*;

public class NbtOutputStream extends OutputStream {

    private NbtTag _tag;
    public NbtOutputStream(NbtTag tag) {
        _tag = tag;
    }

    @Override
    public void write(int b) throws IOException {

    }
}
