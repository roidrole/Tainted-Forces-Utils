package roidrole.tfutils;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import thaumcraft.api.items.ItemsTC;

import javax.annotation.Nonnull;

@JEIPlugin
public class TFUtilsJEIPlugin implements IModPlugin {
	@Override
	public void register(@Nonnull IModRegistry registry){

		registry.addRecipeCatalyst(new ItemStack(ItemsTC.alumentum), "inworldcrafting.explode_item", "inworldcrafting.exploding_blocks");

	}

}
