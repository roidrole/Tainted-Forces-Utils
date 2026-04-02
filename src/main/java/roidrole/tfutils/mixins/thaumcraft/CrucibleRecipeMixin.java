package roidrole.tfutils.mixins.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import roidrole.tfutils.ICrucibleRecipeExpansion;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.Objects;

@Mixin(CrucibleRecipe.class)
public abstract class CrucibleRecipeMixin implements ICrucibleRecipeExpansion {
	@Shadow(remap = false)
	public int hash;

	@Shadow(remap = false)
	private String research;

	@Shadow(remap = false)
	private ItemStack recipeOutput;

	@Shadow(remap = false)
	private Ingredient catalyst;

	@Unique
	private ResourceLocation tfutils_location;

	/**
	 * @author roidrole
	 * @reason Fix persistency issues
	 */
	@Overwrite(remap = false)
	private void generateHash(){
		if(this.tfutils_location == null) {
			this.hash = Objects.hash(this.research, this.catalyst, this.recipeOutput);
		}
		else {
			this.hash = this.tfutils_location.hashCode();
		}
	}

	@Override
	public void tfutils_setResourceLocation(ResourceLocation location) {
		this.tfutils_location = location;
	}
}
