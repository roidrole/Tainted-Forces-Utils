package roidrole.tfutils.config;

import net.minecraftforge.common.config.Config;
import roidrole.tfutils.Tags;

@Config(
	modid = Tags.MOD_ID,
	name = Tags.MOD_ID + "/general"
)
public class TFUtilsConfig {

	@Config.Comment("How much mana the hydrogeanas will generate every 3 ticks (2 if raining)")
	public static int hydrogenanasPowerMultiplier = 3;

	public static float rockTaintificationThreashold = 0.2f;

	@Config.Comment({
		"Minimum value of taintEvo gamerule for taint to eat metals and glass.",
		"rockTaintificationThreashold must also be respected for this to happen."
	})
	public static int metalMinEvo = 10000000;
}
