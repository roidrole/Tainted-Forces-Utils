package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.recipes.UnmoldRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(UnmoldRecipe.class)
public abstract class UnmoldRecipeMixin {
	@ModifyConstant(
		//getCraftingResult
		method = "func_77572_b",
		constant = @Constant(intValue = 100),
		remap = false
	)
	public int increaseCapacity(int constant){
		return 144;
	}
}
