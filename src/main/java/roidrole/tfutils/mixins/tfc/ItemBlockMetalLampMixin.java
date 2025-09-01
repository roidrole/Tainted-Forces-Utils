package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.items.itemblock.ItemBlockMetalLamp;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ItemBlockMetalLamp.class)
public class ItemBlockMetalLampMixin {
	/**
	 * @author roidrole
	 * @reason increase value of a liquid ingot to 144
	 */
	@Overwrite(remap = false)
	public int getSmeltAmount(ItemStack stack){
		return 144;
	}
}
