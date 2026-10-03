package roidrole.tfutils.mixins.bettermineshafts;

import com.yungnickyoung.minecraft.bettermineshafts.init.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModConfig.class)
public abstract class ModConfigMixin {
	@Redirect(
		method = "initCustomFiles",
		at = @At(
			value = "INVOKE",
			target = "Lcom/yungnickyoung/minecraft/bettermineshafts/init/ModConfig;loadVariantsJSON()V"
		),
		remap = false
	)
	private static void skipLoadingVariantJson(){
		//No-op
	}
}
