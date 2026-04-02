package roidrole.tfutils.mixins.thaumcraft;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roidrole.tfutils.ICrucibleRecipeExpansion;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.crafting.CrucibleRecipe;

@Mixin(ThaumcraftApi.class)
public abstract class ThaumcraftApiMixin {
	@Inject(
		method = "addCrucibleRecipe",
		at = @At("HEAD"),
		remap = false
	)
	private static void saveResLoc(ResourceLocation registry, CrucibleRecipe recipe, CallbackInfo ci){
		((ICrucibleRecipeExpansion) recipe).tfutils_setResourceLocation(registry);
	}
}
