package com.copperpenguin96.smartnbt;

import com.copperpenguin96.smartnbt.tags.*;
import java.io.*;

public class Example {

    public void simple() throws IOException {
        /*
        * This is not an actual object that is used in any situation. It is made for example purposes only.
        * [Compound] (User)
        *   [Int] (Days Played: 15)
        *   [String] (UserName: Alex96)
        *   [Byte] (Op Level: 4)
        *   [Byte] (IsHardcore: 0)
        * [End]
        *
         */

        CompoundTag userTag = new CompoundTag("User");

        userTag.add(new IntTag("Days Played", 15));
        userTag.add(new StringTag("UserName", "Alex96"));
        userTag.add(new ByteTag("Op Level", (byte)4));
        userTag.add(new BoolTag("IsHardcore", false));

        NbtFile file = new NbtFile(new File("example.nbt"), userTag);
        file.save();
    }
}
