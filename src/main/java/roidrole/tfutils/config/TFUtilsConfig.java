package roidrole.tfutils.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import roidrole.tfutils.Tags;
import thaumcraft.api.aspects.Aspect;

import java.util.Objects;

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

	@Config.Ignore
	public static Aspect[] cloneAspects;

	@Config.Comment({
		"Which essentia are consumed by the recipe. They are progressed in order, looping around on finish",
		"The amount of essentia required for the craft is the required RF in Sync's config."
	})
	public static String[] cloneAspectsStrings = {};

	static {
		MinecraftForge.EVENT_BUS.register(TFUtilsConfig.class);
	}

	@SubscribeEvent
	public static void handleConfigUpdate(ConfigChangedEvent event){
		if(!Objects.equals(event.getModID(), Tags.MOD_ID)){
			return;
		}
		if(cloneAspectsStrings.length == 0){
			cloneAspectsStrings = new String[]{"humanus"};
		}
		cloneAspects = new Aspect[cloneAspectsStrings.length];
		for (int i = 0; i < cloneAspectsStrings.length; i++) {
			cloneAspects[i] = Aspect.getAspect(cloneAspectsStrings[i]);
		}
	}
}
