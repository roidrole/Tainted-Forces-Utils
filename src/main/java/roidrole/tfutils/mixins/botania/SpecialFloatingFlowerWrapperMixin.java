package roidrole.tfutils.mixins.botania;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.client.integration.jei.crafting.SpecialFloatingFlowerWrapper;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.item.ModItems;

import java.util.List;

@Mixin(SpecialFloatingFlowerWrapper.class)
public abstract class SpecialFloatingFlowerWrapperMixin {
	@Shadow(remap = false)
	@Final
	private List<List<ItemStack>> inputs;

	@Inject(
		method = "<init>",
		at = @At("RETURN"),
		remap = false
	)
	private void usePastureSeed(SpecialFloatingFlowerRecipe recipe, CallbackInfo ci){
		NonNullList<ItemStack> list = NonNullList.create();
		ModItems.grassSeeds.getSubItems(ModItems.grassSeeds.getCreativeTab(), list);
		this.inputs.set(1, list);
	}
}
