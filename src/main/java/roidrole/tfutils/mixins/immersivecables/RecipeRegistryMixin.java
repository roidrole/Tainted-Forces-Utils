package roidrole.tfutils.mixins.immersivecables;

import de.sanandrew.mods.immersivecables.recipes.RecipeRegistry;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(RecipeRegistry.class)
public abstract class RecipeRegistryMixin {
	/**
	 * @author roidrole
	 * @reason remove crash when removing AE2 items
	 */
	@Overwrite(remap = false)
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event){
		//NO-OP
	}
}
