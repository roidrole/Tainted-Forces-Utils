package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.client.particle.TFCParticles;
import net.dries007.tfc.objects.blocks.wood.BlockLeavesTFC;
import net.dries007.tfc.objects.blocks.wood.BlockLogTFC;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import roidrole.tfutils.utils.MutablerBlockPos;

import static net.dries007.tfc.Constants.RNG;

@Mixin(BlockLeavesTFC.class)
public abstract class BlockLeavesTFCMixin extends BlockLeaves {
	@Shadow(remap = false)
	@Final
	public Tree wood;

	@Unique
	private static final MutablerBlockPos tfutils_decayPos = new MutablerBlockPos();

	/**
	 * @author roidrole
	 * @reason Optimize this shit
	 */
	@Overwrite(remap = false)
	private void doLeafDecay(World world, BlockPos posIn, IBlockState state)
	{
		if (world.isRemote || !state.getValue(DECAYABLE)){
			return;
		}

		//Scanning for a wood log
		final BlockLogTFC log = BlockLogTFC.get(wood);
		final int minDy = -wood.getMaxDecayDistance();
		tfutils_decayPos.setPos(posIn);
		tfutils_decayPos.addY(minDy - 1);
		for (int dy = minDy; dy <= -minDy; dy++) {
			tfutils_decayPos.addY();
			int minDx = minDy + Math.abs(dy);
			tfutils_decayPos.addX(minDx - 1);
			for (int dx = minDx; dx <= -minDx; dx++) {
				tfutils_decayPos.addX();
				int minDz = minDx + Math.abs(dx);
				tfutils_decayPos.addZ(minDz - 1);
				for (int dz = minDz; dz <= -minDz; dz++) {
					tfutils_decayPos.addZ();
					if(!world.isBlockLoaded(tfutils_decayPos)){
						continue;
					}
					if(world.getBlockState(tfutils_decayPos).getBlock() == log){
						return;
					}
				}
				tfutils_decayPos.addZ(minDz);
			}
			tfutils_decayPos.addX(minDx);
		}
		//Not worth updating Y pos here

		world.setBlockToAir(posIn);
		int particleScale = 10;
		double x = posIn.getX();
		double y = posIn.getY();
		double z = posIn.getZ();
		for (int i = 1; i < RNG.nextInt(4); i++)
		{
			switch (RNG.nextInt(4))
			{
				case 1:
					TFCParticles.LEAF1.sendToAllNear(world, x + RNG.nextFloat() / particleScale, y - RNG.nextFloat() / particleScale, z + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, -0.15D + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, 90);
					break;
				case 2:
					TFCParticles.LEAF2.sendToAllNear(world, x + RNG.nextFloat() / particleScale, y - RNG.nextFloat() / particleScale, z + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, -0.15D + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, 70);
					break;
				case 3:
					TFCParticles.LEAF3.sendToAllNear(world, x + RNG.nextFloat() / particleScale, y - RNG.nextFloat() / particleScale, z + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, -0.15D + RNG.nextFloat() / particleScale, (RNG.nextFloat() - 0.5) / particleScale, 80);
					break;
			}
		}
	}
}
