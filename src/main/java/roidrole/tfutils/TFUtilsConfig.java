package roidrole.tfutils;

import net.minecraftforge.common.config.Config;

@Config(modid = Tags.MOD_ID)
public class TFUtilsConfig {

	@Config.Comment({
		"Due to a bug between Thaumcraft, BASM and CNPCs, the aspect list gets messed up",
		"As a fix, this tweaks override Thaumic Speedup's check for overriding the aspect cache",
		"That way, the items aren't scanned, and the bug has no effect",
		"Disable once when adding new mods, and never have it enabled at the same time as delayItemStackCapabilityInit"
	})
	public static boolean regenAspectCache = false;
}
