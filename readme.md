# SmartNBT

A light-weight java solution for reading/writing NBT (Named Binary Tag) as used with Minecraft. Works with all versions of minecraft.

## Easy to Use
SmartNBT is super easy to use. Each NBT tag has its own object. Here is an example Compound Tag based off a simple object.

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

Notice the BoolTag object. This is an unofficial tag that wraps the tag for byte. This makes it easier for reading true/false bytes represented as 0/1. Easier readability!

## Maven

SmartNBT is available via external jar or through maven. To use with maven, in pom.xml, add this repository:

    <repository>
	    <id>repsy</id>
	    <name>CopperPenguin on Repsy</name>
	    <url>https://repo.repsy.io/mvn/copperpenguin96/rusty-mineshaft</url>
	</repository>
And for your dependency:

    <dependency>
	    <groupId>com.copperpenguin96</groupId>
	    <artifactId>smartnbt</artifactId>
	    <version>1.0</version>
    </dependency>
 
Snapshot releases are available through github packaging.

## Contributing?

Feel free to open any issues or submit any pull requests.