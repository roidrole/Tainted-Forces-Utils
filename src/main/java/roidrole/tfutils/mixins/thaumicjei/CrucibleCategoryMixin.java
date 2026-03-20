package roidrole.tfutils.mixins.thaumicjei;

import com.buuz135.thaumicjei.category.CrucibleCategory;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IIngredientType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.List;

@Mixin(CrucibleCategory.CrucibleWrapper.class)
public abstract class CrucibleCategoryMixin {
	@Redirect(
		method = "getIngredients",
		at = @At(
			value = "INVOKE",
			target = "Lmezz/jei/api/ingredients/IIngredients;setInputs(Lmezz/jei/api/recipe/IIngredientType;Ljava/util/List;)V"
		),
		remap = false
	)
	private <T> void doubleList(IIngredients instance, IIngredientType<T> type, List<T> ts){
		instance.setInputLists(type, Collections.singletonList(ts));
	}
}
