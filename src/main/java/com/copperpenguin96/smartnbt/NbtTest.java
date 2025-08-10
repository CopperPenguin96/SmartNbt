package com.copperpenguin96.smartnbt;

import com.copperpenguin96.smartnbt.tags.*;

public class NbtTest {

    public static void main(String[] args) {

        NbtTag newTag = NbtTag.create("hi", false);
        System.out.println(newTag);
    }
}
