package roidrole.tfutils.mixins.tfc;

import blusunrize.immersiveengineering.common.IEContent;
import net.dries007.tfc.objects.blocks.devices.BlockBlastFurnace;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(BlockBlastFurnace.class)
public abstract class BlockBlastFurnaceMixin {
	@ModifyVariable(
		method = "<clinit>",
		name = "stoneMatcher",
		at = @At(
			value = "STORE",
			ordinal = 0
		)
	)
	private static Predicate<IBlockState> usePredicate(Predicate<IBlockState> stoneMatcher){
		return o -> o == IEContent.blockStoneDecoration.getStateFromMeta(0);
	}
}
