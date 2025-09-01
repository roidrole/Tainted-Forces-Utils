package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import roidrole.tfutils.MetaItemBlock;

public class NetherMetal extends Block {
	public static NetherMetal BLOCK = new NetherMetal();
	public static ItemBlock ITEM = new MetaItemBlock(BLOCK, "techguns", "nethermetal");

	public PropertyEnum<Type> TYPE;
	public BlockStateContainer stateContainer;

	public NetherMetal() {
		super(Material.IRON);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(8.0f);
		setRegistryName("techguns", "nethermetal");
		setTranslationKey("techguns.nethermetal");

		TYPE = PropertyEnum.create("type", Type.class);
		stateContainer = new BlockStateContainer(this, TYPE);
		setDefaultState(stateContainer.getBaseState());
	}

	@Override
	public BlockStateContainer getBlockState(){
		return stateContainer;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		for (int i = 0; i < Type.values.length; i++) {
			items.add(new ItemStack(BLOCK, 1, i));
		}
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return stateContainer.getBaseState().withProperty(TYPE, Type.values[meta]);
	}

	@Override
	public int getLightValue(IBlockState state){
		if(state.getValue(TYPE) == Type.BORDER_LAVA){
			return 15;
		}
		return 0;
	}

	public enum Type implements IStringSerializable {
		PANEL,
		GRATE1,
		GRATE2,
		GREY_DARK,
		GREY,
		GREY_TILES,
		BORDER_RED,
		PLATE_BLACK,
		PLATE_RED,
		BORDER_LAVA;

		@Override
		public String getName() {
			return name().toLowerCase();
		}
		public static final Type[] values = values();
	}
}
