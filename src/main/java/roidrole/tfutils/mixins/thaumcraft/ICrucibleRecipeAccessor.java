package roidrole.tfutils.mixins.thaumcraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import thaumcraft.api.crafting.CrucibleRecipe;

@Mixin(CrucibleRecipe.class)
public interface ICrucibleRecipeAccessor {

	@Accessor(value = "name", remap = false)
	void setName(String name);
}
