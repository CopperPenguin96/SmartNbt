package com.copperpenguin96.smartnbt;

import com.copperpenguin96.smartnbt.tags.CompoundTag;
import com.copperpenguin96.smartnbt.tags.NbtTag;

import java.io.*;

public class NbtFile {

    private File _file = null;

    public CompoundTag Root = null;

    public NbtFile(String path) {
        this(new File(path));
    }

    public NbtFile(File file) {
        _file = file;
    }

    public NbtFile(String path, CompoundTag root) {
        this(new File(path), root);
    }

    public NbtFile(File file, CompoundTag root) {
        this(file);
        Root = root;
    }

    public void save() throws IOException {
        FileOutputStream outputStream = new FileOutputStream(_file, false);
        Root.writeToStream(outputStream);
        outputStream.flush();
        outputStream.close();
    }


    public static NbtFile read(String path, boolean seekName) throws IOException, NbtFormatException {
        return read(new File(path), seekName);
    }

    public static NbtFile read(String path) throws IOException, NbtFormatException {
        return read(new File(path), true);
    }

    public static NbtFile read(File file, boolean seekName) throws IOException, NbtFormatException {
        if (!file.exists()) throw new FileNotFoundException();

        FileInputStream inputStream = new FileInputStream(file);
        return new NbtFile(file, (CompoundTag) NbtTag.readFromStream(inputStream, seekName));
    }

    public static NbtFile read(File file) throws IOException, NbtFormatException {
        return read(file, true);
    }
}
