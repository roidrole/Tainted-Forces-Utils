package roidrole.tfutils.mixins.thaumicjei;

import com.buuz135.thaumicjei.ThaumcraftJEIPlugin;
import com.buuz135.thaumicjei.category.AspectCompoundCategory;
import com.buuz135.thaumicjei.ingredient.AspectIngredientRender;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AspectCompoundCategory.class)
public abstract class AspectCompoundCategoryMixin {

	/**
	 * @author roidrole
	 * @reason change stuff order
	 */
	@Overwrite(remap = false)
	public void drawExtras(Minecraft minecraft) {
		minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + "=", 32, 6, 0);
		minecraft.fontRenderer.drawString(TextFormatting.DARK_GRAY + "+", 68, 6, 0);
	}

	/**
	 * @author roidrole
	 * @reason change stuff order
	 */
	@Overwrite(remap = false)
	public void setRecipe(IRecipeLayout recipeLayout, AspectCompoundCategory.AspectCompoundWrapper recipeWrapper, IIngredients ingredients) {
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(0, true, new AspectIngredientRender(), 8, 2, 16, 16, 0, 0);
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(1, false, new AspectIngredientRender(), 46, 2, 16, 16, 0, 0);
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).init(2, false, new AspectIngredientRender(), 82, 2, 16, 16, 0, 0);
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(0, ingredients.getInputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(0));
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(1, ingredients.getOutputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(0));
		recipeLayout.getIngredientsGroup(ThaumcraftJEIPlugin.ASPECT_LIST).set(2, ingredients.getOutputs(ThaumcraftJEIPlugin.ASPECT_LIST).get(1));
	}
}
