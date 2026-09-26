package roidrole.tfutils.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
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
		ConfigManager.sync(Tags.MOD_ID, Config.Type.INSTANCE);
		if(cloneAspectsStrings.length == 0){
			cloneAspectsStrings = new String[]{"humanus"};
		}
		cloneAspects = new Aspect[cloneAspectsStrings.length];
		for (int i = 0; i < cloneAspectsStrings.length; i++) {
			cloneAspects[i] = Aspect.getAspect(cloneAspectsStrings[i]);
		}
	}
}
