package roidrole.tfutils.mixins.thaumcraft;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.crafting.CrucibleRecipe;

import java.util.Objects;

@Mixin(CrucibleRecipe.class)
public abstract class CrucibleRecipeMixin {
	@Shadow(remap = false)
	public int hash;

	@Shadow(remap = false)
	private String research;

	@Shadow(remap = false)
	private ItemStack recipeOutput;

	@Shadow(remap = false)
	private Ingredient catalyst;

	@Shadow(remap = false)
	private String name;

	/**
	 * @author roidrole
	 * @reason Fix persistency issues
	 */
	@Overwrite(remap = false)
	private void generateHash(){
		if(this.name.isEmpty()) {
			this.hash = Objects.hash(this.research, this.catalyst, this.recipeOutput);
		}
		else {
			this.hash = this.name.hashCode();
		}
	}
}
