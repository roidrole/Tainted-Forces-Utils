package roidrole.tfutils.mixins.thermal;

import cofh.thermaldynamics.duct.attachments.cover.CoverHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CoverHelper.class)
public class CoverHelperMixin {
	/**
	 * @author roidrole
	 * @reason nuking TD's covers and use AE2's covers
	 */
	@Overwrite(remap = false)
	public static ItemStack getCoverStack(Block block, int meta) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2:facade"));
		if(item == null || block.getRegistryName() == null){
			return ItemStack.EMPTY;
		}
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setInteger("damage", meta);
		nbt.setString("item", block.getRegistryName().toString());
		ItemStack out = new ItemStack(item);
		out.setTagCompound(nbt);
		return out;
	}
}
