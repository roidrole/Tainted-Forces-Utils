package roidrole.tfutils.mixins.tfctech;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tfctech.objects.items.glassworking.ItemBlowpipe;

@Mixin(ItemBlowpipe.class)
public class ItemBlowpipeMixin {
	/**
	 * @author roidrole
	 * @reason standardise ingot to 144 mb and plate to 1 ingot
	 */
	@Overwrite(remap = false)
	public int getSmeltAmount(ItemStack itemStack) {
		return 144;
	}
}
