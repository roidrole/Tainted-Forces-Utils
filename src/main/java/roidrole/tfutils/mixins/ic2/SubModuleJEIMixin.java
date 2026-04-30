package roidrole.tfutils.mixins.ic2;

import ic2.jeiIntegration.SubModule;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;

@Mixin(SubModule.class)
public abstract class SubModuleJEIMixin {
	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/IModRegistry;addRecipeHandlers([Lmezz/jei/api/recipe/IRecipeHandler;)V"
		),
		remap = false
	)
	private void tfutils_noJetpackRecipeHandler(IModRegistry instance, IRecipeHandler[] iRecipeHandlers){
		//NO-OP
	}

	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/IModRegistry;addRecipes(Ljava/util/Collection;)V",
			ordinal = 0
		),
		remap = false
	)
	private void tfutils_noJetpackRecipeCreation(IModRegistry instance, Collection collection){
		//NO-OP
	}
}
