package roidrole.tfutils;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Mod;

import java.io.File;

@Mod(
	modid = "persistency",
	name = "Persistency (Dummy)",
	version = Tags.VERSION
)
public class Persistency {
	static {
		File cachesFolder = new File(Launch.minecraftHome, "cache");
		cachesFolder.mkdirs();
		Launch.blackboard.put("CachesFolderFile", cachesFolder);
	}
}
