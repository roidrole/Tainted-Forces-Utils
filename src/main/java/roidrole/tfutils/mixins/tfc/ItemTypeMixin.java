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
	private void modifyMetalAmount(Metal.ItemType instance, int value) {
		switch(instance.name()){
			//Originally 1/10 of an ingot, we want 1/9
			case "NUGGET": {
				((ItemTypeMixin)(Object)instance).smeltAmount = 16;
				break;
			}
			//Because we want sheets to be 1 ingot instead of 2, we multiply by half.
			case "SHEET":
			case "DOUBLE_SHEET":
			case "TUYERE":
			case "TRAPDOOR":
			case "BOOTS":
			case "UNFINISHED_BOOTS":
			case "GREAVES":
			case "UNFINISHED_GREAVES":
			case "CHESTPLATE":
			case "UNFINISHED_CHESTPLATE":
			case "HELMET":
			case "UNFINISHED_HELMET":
			case "SHIELD":
				((ItemTypeMixin)(Object)instance).smeltAmount = (int) (value * 0.72);
				break;
			default: {
				((ItemTypeMixin) (Object) instance).smeltAmount = (int) (value * 1.44);
				break;
			}
		}
	}
}
