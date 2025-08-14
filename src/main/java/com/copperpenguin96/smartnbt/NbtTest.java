package com.copperpenguin96.smartnbt;

import com.copperpenguin96.smartnbt.tags.*;
import com.copperpenguin96.smartnbt.utils.SNBTParser;

import java.io.InvalidObjectException;

public class NbtTest {

    public static void main(String[] args) {
        CompoundTag userTag = new CompoundTag("User");

        userTag.add(new IntTag("Days Played", 15));
        userTag.add(new StringTag("UserName", "Alex96"));
        userTag.add(new ByteTag("Op Level", (byte)4));
        userTag.add(new BoolTag("IsHardcore", false));

        System.out.println(userTag);

        try {
            SNBTParser.read("");
        } catch (NbtFormatException | InvalidObjectException e) {
            throw new RuntimeException(e);
        }
    }
}
