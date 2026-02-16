package roidrole.tfutils.mixins.thaumcraft;

import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import roidrole.tfutils.TFUtilsConfig;
import thaumcraft.api.ThaumcraftMaterials;
import thaumcraft.api.aura.AuraHelper;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.blocks.IBlockFacing;
import thaumcraft.common.blocks.world.taint.BlockTaintFibre;
import thaumcraft.common.blocks.world.taint.BlockTaintLog;
import thaumcraft.common.blocks.world.taint.TaintHelper;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.entities.monster.tainted.EntityTaintSeed;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.utils.Utils;
import thaumcraft.common.world.aura.AuraHandler;

@Mixin(TaintHelper.class)
public abstract class MixinTaintHelper {
	/**
	 * @author roidrole
	 * @reason I'm not finding injection points in this mess
	 */
	@Overwrite(remap = false)
	public static void spreadFibres(World world, BlockPos pos, boolean ignore) {
		if(!ignore && ModConfig.CONFIG_MISC.wussMode){
			return;
		}
		float mod = AuraHandler.getFluxSaturation(world, pos) * 2.0F;
		if(!ignore && (world.rand.nextFloat() > ModConfig.CONFIG_WORLD.taintSpreadRate * mod)){
			return;
		}
		if(!TaintHelper.isNearTaintSeed(world, pos)){
			return;
		}
		int xx = pos.getX() + world.rand.nextInt(3) - 1;
		int yy = pos.getY() + world.rand.nextInt(3) - 1;
		int zz = pos.getZ() + world.rand.nextInt(3) - 1;
		BlockPos t = new BlockPos(xx, yy, zz);
		if (t.equals(pos)) {
			return;
		}

		IBlockState blockState = world.getBlockState(t);
		Material material = blockState.getBlock().getMaterial(blockState);
		float hardness = blockState.getBlock().getBlockHardness(blockState, world, t);
		if (hardness < 0.0F || hardness > 10.0F) {
			return;
		}

		if (!blockState.getBlock().isLeaves(blockState, world, t) && !material.isLiquid() && (world.isAirBlock(t) || blockState.getBlock().isReplaceable(world, t) || blockState.getBlock() instanceof BlockFlower || blockState.getBlock() instanceof IPlantable) && BlockUtils.isAdjacentToSolidBlock(world, t) && !BlockTaintFibre.isOnlyAdjacentToTaint(world, t)) {
			world.setBlockState(t, BlocksTC.taintFibre.getDefaultState());
			world.addBlockEvent(t, BlocksTC.taintFibre, 1, 0);
			AuraHelper.drainFlux(world, t, 0.01F, false);
			return;
		}

		EntityTaintSeed entity;
		if (blockState.getBlock().isLeaves(blockState, world, t)) {
			EnumFacing face;
			if ((double)world.rand.nextFloat() < 0.6D && (face = BlockUtils.getFaceBlockTouching(world, t, BlocksTC.taintLog)) != null) {
				world.setBlockState(t, BlocksTC.taintFeature.getDefaultState().withProperty(IBlockFacing.FACING, face.getOpposite()));
			} else {
				world.setBlockState(t, BlocksTC.taintFibre.getDefaultState());
				world.addBlockEvent(t, BlocksTC.taintFibre, 1, 0);
				AuraHelper.drainFlux(world, t, 0.01F, false);
			}

			return;
		}

		if (BlockTaintFibre.isHemmedByTaint(world, t) && Utils.isWoodLog(world, t) && blockState.getMaterial() != ThaumcraftMaterials.MATERIAL_TAINT) {
			world.setBlockState(t, BlocksTC.taintLog.getDefaultState().withProperty(BlockTaintLog.AXIS, BlockUtils.getBlockAxis(world, t)));
			return;
		}
		//TODO: Make the max hardness dependent on taint evolution
		//TODO: Make metal taintifiable
		if (BlockTaintFibre.isHemmedByTaint(world, t) && blockState.getBlockHardness(world, t) < 5.0F) {
			if (blockState.getBlock() == Blocks.RED_MUSHROOM_BLOCK || blockState.getBlock() == Blocks.BROWN_MUSHROOM_BLOCK || material == Material.GOURD || material == Material.CACTUS || material == Material.CORAL || material == Material.SPONGE || material == Material.WOOD) {
				world.setBlockState(t, BlocksTC.taintCrust.getDefaultState());
				world.addBlockEvent(t, BlocksTC.taintCrust, 1, 0);
				AuraHelper.drainFlux(world, t, 0.01F, false);
				return;
			}

			if (material == Material.SAND || material == Material.GROUND || material == Material.GRASS || material == Material.CLAY) {
				world.setBlockState(t, BlocksTC.taintSoil.getDefaultState());
				world.addBlockEvent(t, BlocksTC.taintSoil, 1, 0);
				AuraHelper.drainFlux(world, t, 0.01F, false);
				return;
			}

			if (material == Material.ROCK && mod > TFUtilsConfig.rockTaintificationThreashold) {
				world.setBlockState(t, BlocksTC.taintRock.getDefaultState());
				world.addBlockEvent(t, BlocksTC.taintRock, 1, 0);
				AuraHelper.drainFlux(world, t, 0.01F, false);
				return;
			}
		}

		if (
			(blockState.getBlock() == BlocksTC.taintFibre || blockState.getBlock() == BlocksTC.taintSoil || blockState.getBlock() == BlocksTC.taintRock)
		 && world.isAirBlock(t.up())
		 && AuraHelper.getFlux(world, t) >= 5.0F
		 && (double)world.rand.nextFloat() < (double)(ModConfig.CONFIG_WORLD.taintSpreadRate / 100.0F) * 0.33D
		 && TaintHelper.isAtTaintSeedEdge(world, t))
		{
			entity = new EntityTaintSeed(world);
			entity.setLocationAndAngles((float)t.getX() + 0.5F, t.up().getY(), (float)t.getZ() + 0.5F, (float)world.rand.nextInt(360), 0.0F);
			if (entity.getCanSpawnHere()) {
				AuraHelper.drainFlux(world, t, 5.0F, false);
				world.spawnEntity(entity);
			}
		}
	}
}
