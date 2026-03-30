package roidrole.tfutils.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import thaumcraft.api.crafting.CrucibleRecipe;

@Mixin(CrucibleRecipe.class)
public abstract class CrucibleRecipeMixin {
	@Unique
	private static int tfutils_nextId = 0;

	@Redirect(
		method = "<init>",
		at = @At(
			value = "INVOKE",
			target = "Lthaumcraft/api/crafting/CrucibleRecipe;generateHash()V"
		),
		remap = false
	)
	private static void generateID(CrucibleRecipe instance){
		instance.hash = tfutils_nextId++;
	}
}
