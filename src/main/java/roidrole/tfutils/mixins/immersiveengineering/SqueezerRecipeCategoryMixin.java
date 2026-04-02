package roidrole.tfutils.mixins.immersiveengineering;

import blusunrize.immersiveengineering.common.util.compat.jei.squeezer.SqueezerRecipeCategory;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(SqueezerRecipeCategory.class)
public abstract class SqueezerRecipeCategoryMixin {
	@Redirect(
		method = "setRecipe(Lmezz/jei/api/gui/IRecipeLayout;Lblusunrize/immersiveengineering/common/util/compat/jei/squeezer/SqueezerRecipeWrapper;Lmezz/jei/api/ingredients/IIngredients;)V",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/ingredients/IIngredients;getOutputs(Lmezz/jei/api/recipe/IIngredientType;)Ljava/util/List;",
			ordinal = 0
		),
		remap = false
	)
	private static List<List<ItemStack>> checkItem(IIngredients instance, IIngredientType<ItemStack> tiIngredientType){
		return instance.getOutputs(VanillaTypes.ITEM);
	}
}
