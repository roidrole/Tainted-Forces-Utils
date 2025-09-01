package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class NetherSteelSlab extends BlockSlab {
	public static NetherSteelSlab DOUBLE = new NetherSteelSlab(NetherSteel.BLOCK, true);
	public static NetherSteelSlab HALF = new NetherSteelSlab(NetherSteel.BLOCK);
	public static ItemBlock ITEM = new ItemSlab(HALF, HALF, DOUBLE){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};


	public NetherSteelSlab(Block modelBlock) {
		super(modelBlock.getDefaultState().getMaterial());
		this.setCreativeTab(CreativeTabs.DECORATIONS);
		this.setHardness(8.0f);
		this.setRegistryName("bewitchment", "nethersteel_slab");
		this.setTranslationKey("bewitchment.nethersteel_slab");
		this.fullBlock = false;
		this.useNeighborBrightness = true;
	}
	public NetherSteelSlab(Block modelBlock, boolean ignored) {
		super(modelBlock.getDefaultState().getMaterial());
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(8.0f);
		setRegistryName("bewitchment", "nethersteel_slab_double");
		setTranslationKey("bewitchment.nethersteel_slab");
		this.fullBlock = true;
		this.useNeighborBrightness = true;
		this.setCreativeTab(null);
	}

	@Override
	public String getTranslationKey(int meta) {
		return super.getTranslationKey();
	}

	@Override
	public boolean isDouble() {
		return this.fullBlock;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return BlockPurpurSlab.VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return BlockPurpurSlab.Variant.DEFAULT;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return isDouble()?getDefaultState().withProperty(BlockPurpurSlab.VARIANT, BlockPurpurSlab.Variant.DEFAULT) : getDefaultState().withProperty(BlockPurpurSlab.VARIANT, BlockPurpurSlab.Variant.DEFAULT).withProperty(BlockSlab.HALF, meta == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return !isDouble() && state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP ? 1 : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ITEM.getItemFromBlock(HALF);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(HALF);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockSlab.HALF, BlockPurpurSlab.VARIANT);
	}
}
