package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.blocks.wood.BlockLeavesTFC;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;

@Mixin(BlockLeavesTFC.class)
public abstract class BlockLeavesTFCMixin extends BlockLeaves {
	/**
	 * @author roidrole
	 * @reason really, don't do TFC leaf decay
	 */
	@Overwrite
	public void func_180650_b(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		//NO-OP
	}

	/**
	 * @author roidrole
	 * @reason Actually, do vanilla leaf decay
	 */
	@Overwrite
	public void beginLeavesDecay(IBlockState state, World world, BlockPos pos){
		super.beginLeavesDecay(state, world, pos);
	}
}
