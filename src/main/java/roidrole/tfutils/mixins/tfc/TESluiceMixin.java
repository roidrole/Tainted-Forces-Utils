package roidrole.tfutils.mixins.tfc;

import com.llamalad7.mixinextras.sugar.Local;
import net.dries007.tfc.objects.blocks.devices.BlockSluice;
import net.dries007.tfc.objects.te.TEBase;
import net.dries007.tfc.objects.te.TESluice;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(value = TESluice.class, remap = false)
public abstract class TESluiceMixin extends TEBase {
	/**
	 * @author roidrole
	 * @reason Consider vanilla fluids
	 */
	@Nullable
	@Overwrite
	public Fluid getFlowingFluid() {
		if (!hasWorld() || !(world.getBlockState(pos).getBlock() instanceof BlockSluice))
		{
			return null;
		}
		EnumFacing sluiceFacing = world.getBlockState(pos).getValue(BlockHorizontal.FACING);
		BlockPos fluidInputPos = pos.up().offset(sluiceFacing);
		IBlockState state = world.getBlockState(fluidInputPos);
		Block block = state.getBlock();
		if (block instanceof IFluidBlock) {
			return ((IFluidBlock) block).getFluid();
		} else if(block instanceof BlockLiquid) {
			if(state.getMaterial() == Material.WATER){
				return FluidRegistry.WATER;
			} else if (state.getMaterial() == Material.LAVA){
				return FluidRegistry.LAVA;
			}
			return null;
		}
		return null;
	}

	@Inject(
		method = "hasFlow",
		at = @At(
			value = "RETURN",
			ordinal = 2
		),
		cancellable = true
	)
	private void checkVanillaFluids(
		CallbackInfoReturnable<Boolean> cir,
		@Local(name = "block") Block block,
		@Local(name = "fluid") Fluid fluid,
		@Local(name = "frontState") IBlockState frontState
	){
		if (block instanceof BlockLiquid)
		{
			Fluid fluid1 = null;
			if(frontState.getMaterial() == Material.WATER){
				fluid1 = FluidRegistry.WATER;
			} else if (frontState.getMaterial() == Material.LAVA){
				fluid1 = FluidRegistry.LAVA;
			}
			cir.setReturnValue(fluid == fluid1);
		}
	}
}
