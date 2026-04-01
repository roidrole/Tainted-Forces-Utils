package roidrole.tfutils.mixins.thaumicjei;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import mezz.jei.api.IModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThaumcraftJEIPlugin.class)
public abstract class ThaumcraftJEIPluginMixin {
	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/IModRegistry;addRecipeCatalyst(Ljava/lang/Object;[Ljava/lang/String;)V",
			ordinal = 3
		),
		remap = false
	)
	private static void tfutils_controlCatalyst3(IModRegistry instance, Object o, String[] strings){
		//NO-OP
	}
	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/IModRegistry;addRecipeCatalyst(Ljava/lang/Object;[Ljava/lang/String;)V",
			ordinal = 4
		),
		remap = false
	)
	private static void tfutils_controlCatalyst4(IModRegistry instance, Object o, String[] strings){
		//NO-OP
	}
}
