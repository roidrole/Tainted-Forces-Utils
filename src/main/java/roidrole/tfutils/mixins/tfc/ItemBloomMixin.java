package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.objects.items.ItemBloom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

//Currently broken mixin (doesn't seem to do anything, but the bloom will probably get removed from the pack anyway)
@Mixin(ItemBloom.class)
public class ItemBloomMixin {
	@ModifyConstant(
		method = {"getSmeltAmount", "initCapabilities", "func_150895_a"},
		constant = @Constant(intValue = 100),
		remap = false
	)
	public int increaseCapacity(int constant){
		return 144;
	}

	@ModifyConstant(
		method = {"func_150895_a"},
		constant = @Constant(intValue = 400),
		remap = false
	)
	public int increaseMaxJei(int constant){
		return 576;
	}
}
