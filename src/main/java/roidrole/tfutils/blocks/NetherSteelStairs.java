package roidrole.tfutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

public class NetherSteelStairs extends BlockStairs {
	public static Block BLOCK = new NetherSteelStairs(NetherSteel.BLOCK);
	public static ItemBlock ITEM = new ItemBlock(BLOCK){{
		setRegistryName(block.getRegistryName());
		setTranslationKey(block.getTranslationKey());
	}};

	public NetherSteelStairs(Block modelBlock) {
		super(modelBlock.getDefaultState());
		setCreativeTab(CreativeTabs.DECORATIONS);
		setHardness(8.0f);
		setRegistryName("bewitchment", "nethersteel_stairs");
		setTranslationKey("bewitchment.nethersteel_stairs");
	}
}
