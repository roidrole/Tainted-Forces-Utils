package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class NetherSteelWall extends BlockWall {
	public static Block BLOCK = new NetherSteelWall(NetherSteel.BLOCK);
	public static ItemBlock ITEM = new ItemBlock(BLOCK){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};

	public NetherSteelWall(Block modelBlock) {
		super(modelBlock);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(8.0f);
		setRegistryName("bewitchment", "nethersteel_wall");
		setTranslationKey("bewitchment.nethersteel_wall");
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this));
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos).withProperty(VARIANT, EnumType.NORMAL);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT, UP, NORTH, SOUTH, WEST, EAST);
	}
}
