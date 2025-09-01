package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;

public class NetherSteelFence extends BlockFence {
	public static Block BLOCK = new NetherSteelFence(NetherSteel.BLOCK);
	public static ItemBlock ITEM = new ItemBlock(BLOCK){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};

	public NetherSteelFence(Block modelBlock) {
		super(modelBlock.getDefaultState().getMaterial(), modelBlock.getDefaultState().getMaterial().getMaterialMapColor());
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(8.0f);
		setRegistryName("bewitchment", "nethersteel_fence");
		setTranslationKey("bewitchment.nethersteel_fence");
	}

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
