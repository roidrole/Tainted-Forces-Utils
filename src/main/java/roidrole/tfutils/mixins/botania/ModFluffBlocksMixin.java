package roidrole.tfutils.mixins.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.common.block.ModFluffBlocks;

@Mixin(ModFluffBlocks.class)
public abstract class ModFluffBlocksMixin {
	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lvazkii/botania/api/state/enums/BiomeStoneVariant;values()[Lvazkii/botania/api/state/enums/BiomeStoneVariant;"
		),
		remap = false
	)
	private static BiomeStoneVariant[] removeStoneVariants(){
		return new BiomeStoneVariant[0];
	}


	@Redirect(
		method = "register",
		at = @At(
			value = "INVOKE",
			target = "Lvazkii/botania/api/state/enums/BiomeBrickVariant;values()[Lvazkii/botania/api/state/enums/BiomeBrickVariant;"
		),
		remap = false
	)
	private static BiomeBrickVariant[] removeStoneBrickVariants(){
		return new BiomeBrickVariant[0];
	}

	@ModifyConstant(
		method = "registerItemBlocks",
		constant = @Constant(intValue = 24),
		remap = false
	)
	private static int noRegisteringMetamorphicItems(int constant){
		return 0;
	}
}
