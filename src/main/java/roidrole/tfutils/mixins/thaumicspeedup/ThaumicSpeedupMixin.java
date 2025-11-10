package roidrole.tfutils.mixins.thaumicspeedup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import roidrole.tfutils.TFUtilsConfig;
import zone.rong.thaumicspeedup.ThaumicSpeedup;

import java.util.Map;

@Mixin(ThaumicSpeedup.class)
public abstract class ThaumicSpeedupMixin {
	@Redirect(
		method = "construct",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
		),
		remap = false
	)
	Object configForReloadingCache(Map instance, Object key, Object defaultValue){
		return !TFUtilsConfig.regenAspectCache;
	}
}
