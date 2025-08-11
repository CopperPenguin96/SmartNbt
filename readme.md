# SmartNBT

A light-weight java solution for reading/writing NBT (Named Binary Tag) as used with Minecraft. Works with all versions of minecraft. Includes SNBT support as well.

## Easy to Use
SmartNBT is super easy to use. Each NBT tag has its own object. Here is an example Compound Tag based off a simple object. SNBT Bool Tags are also handled and can be used as an unofficial NBT type too.

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
	
Needing to convert to SNBT? No problem!

	// outputs {"Days Played":15,UserName:"Alex96","Op Level":4b,IsHardcore:false}
	System.out.println(userTag);
	
Needing to convert from SNBT? Easy!

	String snbt = "some random SNBT bullshit here";
	CompoundTag compoundTag = SNBTParser.read(snbt);

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