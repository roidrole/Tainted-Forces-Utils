package roidrole.tfutils.blocks;

import com.deeperdepths.common.DeeperDepthsSoundTypes;
import com.deeperdepths.common.blocks.BlockCopperLantern;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
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

public class Lantern extends Block {
	public static Lantern BLOCK = new Lantern();
	public static ItemBlock ITEM = new ItemBlock(BLOCK){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};

	protected static final AxisAlignedBB CRYSTAL_AABB = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.6D, 0.7D);


	public Lantern() {
		super(Material.IRON);
		setRegistryName(Tags.MOD_ID, "lantern");
		setTranslationKey(Tags.MOD_ID + ".lantern");
		setCreativeTab(CreativeTabs.DECORATIONS);
		setSoundType(DeeperDepthsSoundTypes.LANTERN);
		setLightLevel(1);
	}
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return isValidBlock(worldIn, pos, worldIn.getBlockState(pos.up()), true)
			|| isValidBlock(worldIn, pos, worldIn.getBlockState(pos.down()), false);
	}


	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CRYSTAL_AABB;
	}

	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(BlockCopperLantern.HANGING, facing == EnumFacing.DOWN);
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		this.checkAndDropBlock(worldIn, pos, state);
	}

	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
		return state.getValue(BlockCopperLantern.HANGING) ? isValidBlock(worldIn, pos, worldIn.getBlockState(pos.up()), true) : isValidBlock(worldIn, pos, worldIn.getBlockState(pos.down()), false);
	}


	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockCopperLantern.HANGING);
	}


	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		if(state.getValue(BlockCopperLantern.HANGING)){
			return 1;
		} else {
			return 0;
		}
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		boolean hanging;
		if(meta == 0){
			hanging = false;
		} else {
			hanging = true;
		}
		return this.getDefaultState().withProperty(BlockCopperLantern.HANGING, hanging);
	}

	public boolean isValidBlock(World worldIn, BlockPos pos, IBlockState state, boolean hanging) {
		Block block = state.getBlock();
		if (block instanceof BlockShulkerBox || block instanceof BlockAnvil || block.canPlaceTorchOnTop(state, worldIn, pos)) {
			return true;
		}
		BlockFaceShape shape = state.getBlockFaceShape(worldIn, pos, hanging ? EnumFacing.DOWN : EnumFacing.UP);
		return shape != BlockFaceShape.BOWL && shape != BlockFaceShape.UNDEFINED;
	}

}
