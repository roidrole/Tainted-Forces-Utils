package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.blocks.devices.BlockCharcoalForge;
import net.dries007.tfc.util.block.Multiblock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Predicate;

@Mixin(BlockCharcoalForge.class)
public abstract class BlockCharcoalForgeMixin {
	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/dries007/tfc/util/block/Multiblock;match(Lnet/minecraft/util/math/BlockPos;Ljava/util/function/Predicate;)Lnet/dries007/tfc/util/block/Multiblock;"
		),
		remap = false
	)
	private static Multiblock tfutils_dontCheckTopBlock(Multiblock instance, BlockPos posOffset, Predicate<IBlockState> stateMatcher){
		return instance;
	}


	@Redirect(
		method = "<clinit>",
		at = @At(
			value = "INVOKE",
			target = "Lnet/dries007/tfc/util/block/Multiblock;matchOneOf(Lnet/minecraft/util/math/BlockPos;Lnet/dries007/tfc/util/block/Multiblock;)Lnet/dries007/tfc/util/block/Multiblock;"
		),
		remap = false
	)
	private static Multiblock tfutils_dontCheckSky(Multiblock instance, BlockPos baseOffset, Multiblock subMultiblock){
		return instance;
	}
}
