package roidrole.tfutils;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MetaItemBlock extends ItemBlock {
	public MetaItemBlock(Block block, String modid, String name) {
		super(block);
		setHasSubtypes(true);
		setRegistryName(modid, name);
		setTranslationKey(modid+"."+name);
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}

	@Override
	public String getTranslationKey(ItemStack stack){
		return this.getTranslationKey() + "." + stack.getItemDamage();
	}
}
