package roidrole.tfutils.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.BlockCopperChain;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import roidrole.tfutils.Tags;

public class Chain extends BlockRotatedPillar {
	public static Chain BLOCK = new Chain();
	public static ItemBlock ITEM = new ItemBlock(BLOCK){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};

	//Because they are protected in BlockCopperChain :(
	protected static final AxisAlignedBB CHAIN_AABB_Y = new AxisAlignedBB(0.6D, 0.0D, 0.6D, 0.4D, 1.0D, 0.4D);
	protected static final AxisAlignedBB CHAIN_AABB_X = new AxisAlignedBB(0.0D, 0.4D, 0.6D, 1.0D, 0.6D, 0.4D);
	protected static final AxisAlignedBB CHAIN_AABB_Z = new AxisAlignedBB(0.6D, 0.4D, 0.0D, 0.4D, 0.6D, 1.0D);

	public Chain() {
		super(Material.IRON);
		setRegistryName(Tags.MOD_ID, "chain");
		setTranslationKey(Tags.MOD_ID + ".chain");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setSoundType(DeeperDepthsSoundTypes.CHAIN);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		switch (state.getValue(BlockCopperChain.AXIS))
		{
			case X:
				return CHAIN_AABB_X;
			case Z:
				return CHAIN_AABB_Z;
			default:
				return CHAIN_AABB_Y;
		}
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(BlockCopperChain.AXIS, facing.getAxis());
	}

	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if (state.getValue(AXIS) == EnumFacing.Axis.Y) {
			return BlockFaceShape.MIDDLE_POLE;
		} else {
			return BlockFaceShape.UNDEFINED;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS);
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AXIS).ordinal();
	}
}
