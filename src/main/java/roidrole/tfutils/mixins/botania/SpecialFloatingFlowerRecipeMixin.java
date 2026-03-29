package roidrole.tfutils.mixins.botania;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.item.ModItems;

@Mixin(SpecialFloatingFlowerRecipe.class)
public abstract class SpecialFloatingFlowerRecipeMixin {
	@Redirect(
		//matches
		method = "func_77569_a",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/item/Item;func_150898_a(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;",
			ordinal = 0
		),
		remap = false
	)
	private static Item usePastureSeed(Block block){
		return ModItems.grassSeeds;
	}
}
