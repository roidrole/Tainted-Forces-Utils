package roidrole.tfutils.mixins.tfc;

import net.dries007.tfc.api.types.Metal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Metal.ItemType.class)
public class ItemTypeMixin {
	@Mutable
	@Final
	@Shadow(remap = false)
	private int smeltAmount;

	@Redirect(
		method = "<init>(Ljava/lang/String;IZIILjava/util/function/BiFunction;Z[Ljava/lang/String;)V",
		at = @At(
			value = "FIELD",
			target = "Lnet/dries007/tfc/api/types/Metal$ItemType;smeltAmount:I"
		),
		remap = false
	)
	private void increaseMetalAmount(Metal.ItemType instance, int value) {
		if(value == 10){
			value = 16;
		} else {
			value = (int) (value * 1.44);
		}
		((ItemTypeMixin)(Object)instance).smeltAmount = value;
	}
}
