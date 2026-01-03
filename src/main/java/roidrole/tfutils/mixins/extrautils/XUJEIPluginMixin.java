package roidrole.tfutils.mixins.extrautils;

import com.rwtema.extrautils2.crafting.jei.XUJEIPlugin;
import mezz.jei.api.IModRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(XUJEIPlugin.class)
public abstract class XUJEIPluginMixin {
	//Extra Utils recomputes the burn list, wasting 500 ms. Let us not
	@Inject(
		method = "register",
		remap = false,
		at = @At("HEAD")
	)
	private static void updateBurnList(IModRegistry registry, CallbackInfo ci){
		IBurnListAccessor.setStacks(registry.getIngredientRegistry().getFuels());
	}
}
