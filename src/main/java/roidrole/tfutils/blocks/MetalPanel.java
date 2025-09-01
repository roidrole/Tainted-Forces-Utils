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

public class MetalPanel extends Block {
	public static MetalPanel BLOCK = new MetalPanel();
	public static ItemBlock ITEM = new MetaItemBlock(BLOCK, "techguns", "metalpanel");

	public PropertyEnum<Type> TYPE;
	public BlockStateContainer stateContainer;

	public MetalPanel() {
		super(Material.IRON);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		setHardness(8.0f);
		setRegistryName("techguns", "metalpanel");
		setTranslationKey("techguns.metalpanel");

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

	public enum Type implements IStringSerializable {
		CONTAINER_RED,
		CONTAINER_GREEN,
		CONTAINER_BLUE,
		CONTAINER_ORANGE,
		PANEL_LARGE_BORDER,
		STEELFRAME_BLUE,
		STEELFRAME_DARK,
		STEELFRAME_SCAFFOLD;

		@Override
		public String getName() {
			return name().toLowerCase();
		}
		public static final Type[] values = values();
	}
}
